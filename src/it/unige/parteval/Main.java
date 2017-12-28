package it.unige.parteval;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.unige.automata.Automaton;
import it.unige.automata.State;
import it.unige.automata.Transition;
import it.unige.automata.impl.DFAutomatonImpl;
import it.unige.automata.impl.NFAutomatonImpl;
import it.unige.automata.impl.StateImpl;
import it.unige.automata.impl.TransitionImpl;
import it.unige.automata.util.AutomataTextualInterface;
import it.unige.lts.LTS;
import it.unige.mu.*;
import it.unige.parteval.test.Test;

public class Main {
	
	private final static String help = ""
			+ "Usage: java -jar pests.jar INPUT [OPTIONS]\n"
			+ "\n"
			+ "INPUT must be an existing file containing a textual description of a finite state agent\n"
			+ "\n"
			+ "OPTIONS are a subset of the following:\n"
			+ "-o=FILE\t\t\t write output on FILE. Writes on standard output if not specified.\n"
			+ "-s=FILE\t\t\t read specification from FILE. Uses FALSE if not specified.\n"
			+ "-a={v1,...,vN}\t\t use v1,...,vN as synchronization actions. Uses {} if not specified.\n"
			+ "-f=txt|svg|png|pdf\t use the specified output format. Omitting this option is equivalent to -f=txt.\n"
			+ "-h\t\t\t print this message and exit.\n"; 
	
	private final static String error = "Invalid arguments";

	public static void main(String[] args) {
		
		Options opt = new Options();
		
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
				System.out.println(e);
				System.exit(1);
			}
			DFAutomatonImpl A = AutomataTextualInterface.read(buffer);
			
			DFAutomatonImpl P;
			
			if(opt.specification == null) {
				StateImpl i = new StateImpl("FF");
				P = new DFAutomatonImpl(i);
			} else {
				try {
					buffer = readFile(opt.input);
				} catch(IOException e) {
					System.out.println(e);
					System.exit(1);
				}
				P = AutomataTextualInterface.read(buffer);
			}
			
			// Set<String> G = getGamma();
			
				
			// NFAutomatonImpl Pp = Projection.partialA(P, A, getSigma(), G);
			
			// P = Projection.unify(Pp, G);
		}
		
	}
	
	private static String readFile(String path) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded);
	}

	private enum ArgsType {VALID, INVALID, HELP};
	
	private static ArgsType validate(String[] args, Options opt) {
		if(args.length < 2) {
			return ArgsType.INVALID;
		}
		else {
			
			if(args[1].startsWith("-h")) {
				return ArgsType.HELP;
			}
			else {
				opt.input = args[1];
			}
			
			for(int i = 2; i < args.length; i++) {
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
				else if(args[i].startsWith("-a")) {
					if(opt.sync_actions != null)
						return ArgsType.INVALID;
					
					opt.sync_actions = args[i].substring(4, args[i].length()-1).split(",");
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
	
	private static class Options {
		String input;
		String output;
		String specification;
		String output_format;
		String[] sync_actions;
	}
}
