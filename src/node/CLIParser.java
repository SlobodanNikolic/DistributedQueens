package node;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import app.AppInfo;
import bootstrap.BootstrapListener;
import bootstrap.BootstrapWorker;
import commands.Command;
import commands.InfoCommand;
import commands.JoinCommand;
import commands.PingCommand;



/**
 * A simple CLI parser. Each command has a name and arbitrary arguments.
 * 
 * Currently supported commands:
 * 
 * <ul>
 * <li><code>info</code> - prints information about the current node</li>
 * <li><code>pause [ms]</code> - pauses exection given number of ms - useful when scripting</li>
 * <li><code>ping [id]</code> - sends a PING message to node [id] </li>
 * <li><code>broadcast [text]</code> - broadcasts the given text to all nodes</li>
 * <li><code>causal_broadcast [text]</code> - causally broadcasts the given text to all nodes</li>
 * <li><code>print_causal</code> - prints all received causal broadcast messages</li>
 * <li><code>stop</code> - stops the servent and program finishes</li>
 * </ul>
 * 
 * @author bmilojkovic
 *
 */
public class CLIParser implements Runnable{

	private volatile boolean working = true;
	private SimpleListener listener;
	private NodeWorker worker;
	private BootstrapWorker bootstrap;
	
	private final List<Command> commandList;
	
	public CLIParser(SimpleListener listener) {
		this.listener = listener;
		
		this.commandList = new ArrayList<>();
		
		commandList.add(new InfoCommand(bootstrap));
		commandList.add(new PingCommand());
		commandList.add(new JoinCommand());
	}
	
	public CLIParser(SimpleListener listener, NodeWorker worker) {
		this.listener = listener;
		this.worker = worker;
		
		this.commandList = new ArrayList<>();
		
		commandList.add(new InfoCommand(bootstrap));
		commandList.add(new PingCommand());
		commandList.add(new JoinCommand());
	}
	
	public CLIParser(SimpleListener listener, BootstrapWorker worker) {
		this.listener = listener;
		this.bootstrap = worker;
		
		this.commandList = new ArrayList<>();
		
		commandList.add(new InfoCommand(bootstrap));
		commandList.add(new PingCommand());
		commandList.add(new JoinCommand());
	}
	
	@Override
	public void run() {
		Scanner sc = new Scanner(System.in);
		
		while (working) {
			String commandLine = sc.nextLine();
			
			int spacePos = commandLine.indexOf(" ");
			
			String commandName = null;
			String commandArgs = null;
			if (spacePos != -1) {
				commandName = commandLine.substring(0, spacePos);
				commandArgs = commandLine.substring(spacePos+1, commandLine.length());
			} else {
				commandName = commandLine;
			}
			
			boolean found = false;
			
			for (Command cliCommand : commandList) {
				if (cliCommand.commandName().equals(commandName)) {
					cliCommand.execute(commandArgs);
					found = true;
					break;
				}
			}
			
			if (!found) {
				NodeConfig.getInstance().timestampedErrorPrint("Unknown command: " + commandName);
			}
		}
		
		sc.close();
	}
	
	public void stop() {
		this.working = false;
		
	}
}
