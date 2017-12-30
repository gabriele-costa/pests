package it.unige.parteval;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import it.unige.automata.impl.DFAutomatonImpl;
import it.unige.automata.impl.NFAutomatonImpl;
import it.unige.automata.impl.StateImpl;
import it.unige.automata.util.AutomataTextualInterface;
import it.unige.automata.util.Printer;

public class Main {
	
	private final static String help = ""
			+ "Partial Evaluator of Simple Transition Systems (PESTS)\n"
			+ "Usage: java -jar pests.jar INPUT [OPTIONS]\n"
			+ "\n"
			+ "INPUT must be an existing file containing a textual description of a finite state agent\n"
			+ "\n"
			+ "OPTIONS are a subset of the following:\n"
			+ "-o=FILE\t\t\t write output on FILE. Writes on standard output if not specified and option -f is set to txt.\n"
			+ "-s=FILE\t\t\t read specification from FILE. Uses FALSE if not specified.\n"
			+ "-a=v1,...,vN\t\t use v1,...,vN as synchronous actions. Uses the empty set if not specified.\n"
			+ "-b=v1,...,vN\t\t use v1,...,vN as asynchronous actions. Uses the empty set if not specified.\n"
			+ "-f=txt|svg|png|pdf\t use the specified output format. Omitting this option is equivalent to -f=txt.\n"
			+ "-v\t\t\t activate verbose output.\n"
			+ "-h\t\t\t print this message and exit.\n"; 
	
	private final static String error = "Invalid arguments";

	public static void main(String[] args) {
		
		log("Process started.");
		
		Options opt = new Options();
		
		log("Parsing inputs.");
		
		ArgsType val = validate(args, opt);
		
		if(val == ArgsType.HELP) {
			System.out.println(help);
			System.exit(0);
		}
		else if(val == ArgsType.INVALID) {
			System.out.println(error);
			System.out.println(help);
			System.exit(1);
		}
		else {
			
			String buffer = "";
			try {
				buffer = readFile(opt.input);
			} catch(IOException e) {
				System.out.println("Could not open " + opt.input);
				System.exit(1);
			}
			DFAutomatonImpl A = AutomataTextualInterface.read(buffer);
			
			DFAutomatonImpl P = null;
			
			if(opt.specification == null) {
				StateImpl i = new StateImpl("FF");
				P = new DFAutomatonImpl(i);
			} else {
				try {
					buffer = readFile(opt.specification);
				} catch(IOException e) {
					System.out.println("Could not open " + opt.specification);
					System.exit(1);
				}
				P = AutomataTextualInterface.read(buffer);
			}
			
			Set<String> G = new HashSet<String>();
			
			if(opt.sync_actions != null)
				G.addAll(Arrays.asList(opt.sync_actions));
			
			Set<String> S = new HashSet<String>();
			
			if(opt.async_actions != null)
				S.addAll(Arrays.asList(opt.async_actions));
			
			log("Starting partial evaluation.");
			
			long t0 = System.currentTimeMillis();
			
			NFAutomatonImpl Pp = Projection.partialA(P, A, S, G);
			
			long t1 = System.currentTimeMillis();
			log("Partial evaluation: Time=" + (t1-t0) +" ms, States="+P.getStates().size()+", Transitions=" + P.getTransitions().size()); 
			
			DFAutomatonImpl P2 = Projection.unify(Pp, G);
			
			long t2 = System.currentTimeMillis();
			log("Unification: Time=" + (t2-t1) +" ms, States="+P2.getStates().size()+", Transitions=" + P2.getTransitions().size()); 
			
			if(opt.output_format == null)
				opt.output_format = "txt";
			
			log("Writing output.");
			
			String output = "";
			
			if(opt.output_format.equals("txt") || opt.output == null) {
				Printer.type = "txt";
				output = AutomataTextualInterface.write(P2);
			} else {
				Printer.type = opt.output_format;
				Printer.outdir = new File(opt.output).getParent();
				output = Printer.printDotAutomaton(P2, "PartEval");
			}
			
			if(opt.output == null) {
				System.out.println(output);
			}
			else {
				File tmp = new File(Printer.outdir + "PartEval."+ opt.output_format);
				
				try {
					if(!tmp.exists())
						tmp.createNewFile();
					if(opt.output_format.equals("txt"))
						Files.write(tmp.toPath(), output.getBytes(), StandardOpenOption.WRITE);
					else 
						Printer.createDotGraph(output, "PartEval");
					Files.move(tmp.toPath(), (new File(opt.output)).toPath(), StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					System.out.println("Cannot write on file due to: "+e);
				}
			}
			
			System.out.println("Completed.");
		}
		
	}
	
	private static String readFile(String path) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded);
	}

	private enum ArgsType {VALID, INVALID, HELP};
	
	private final static int FIRSTARG = 0;
	
	private static ArgsType validate(String[] args, Options opt) {
		
		if(args.length < FIRSTARG + 1) {
			return ArgsType.INVALID;
		}
		else {
			
			if(args[FIRSTARG].startsWith("-h")) {
				return ArgsType.HELP;
			}
			else {
				opt.input = args[FIRSTARG];
			}
			
			for(int i = FIRSTARG + 1; i < args.length; i++) {
				if(args[i].startsWith("-o")) {
					if(opt.output != null)
						return ArgsType.INVALID;
					
					opt.output = args[i].substring(3);
				}
				else if(args[i].startsWith("-s")) {
					if(opt.specification != null)
						return ArgsType.INVALID;
					
					opt.specification = args[i].substring(3);
				}
				else if(args[i].startsWith("-v")) {
					if(verbose)
						return ArgsType.INVALID;
					
					verbose = true;
				}
				else if(args[i].startsWith("-a")) {
					if(opt.sync_actions != null)
						return ArgsType.INVALID;
					
					opt.sync_actions = args[i].substring(3).split(",");
				}
				else if(args[i].startsWith("-b")) {
					if(opt.async_actions != null)
						return ArgsType.INVALID;
					
					opt.async_actions = args[i].substring(3).split(",");
				}
				else if(args[i].startsWith("-f")) {
					if(opt.output_format != null)
						return ArgsType.INVALID;
					
					opt.output_format = args[i].substring(3);
				}
				else 
					return ArgsType.INVALID;
			}
			
			return ArgsType.VALID;
		}
		
	}
	
	private static boolean verbose = false;
	
	private static void log(String msg) {
		if(verbose) {
			System.out.println(msg);
		}
	}
	
	private static class Options {
		String input;
		String output;
		String specification;
		String output_format;
		String[] sync_actions;
		String[] async_actions;
	}
}
