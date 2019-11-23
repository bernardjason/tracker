package org.bjason.microservices.services;

import org.apache.commons.cli.*;
import org.bjason.microservices.tickets.TicketControlServer;
import org.bjason.microservices.users.UsersControlServer;


public class Main {

    public static void main(String[] args) {
        Options options = new Options();

        options.addOption("e","eureka.hostname", true, "euerka hostname for requests, default localhost");
        options.addOption("p","server.port", true, "listen on port");
        options.addOption("u","eureka.instance.url", true, "euerka url, default http://localhost:1111/eureka");
        options.addOption("x","redishost", true, "redit host, default localhost");
        options.addOption("y","redisport", true, "redit port , default 6379");
        options.addRequiredOption("s","server", true, "type of server, user-control or ticker-control");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine line = parser.parse( options, args );

            getCmdSetProperty(line,"eureka.hostname","localhost");
            getCmdSetProperty(line,"server.port",null);
            getCmdSetProperty(line,"eureka.instance.url","http://localhost:1111/eureka");
            getCmdSetProperty(line,"redishost","localhost");
            getCmdSetProperty(line,"redisport","6379");
            String serverName =  line.getOptionValue("server");
            if (serverName.equals("user-control")) {
                UsersControlServer.main(args);
            } else if (serverName.equals("ticket-control")) {
                TicketControlServer.main(args);
            } else {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp( "spring edge-controller", options );
            }
        }
        catch( ParseException exp ) {
            System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "spring-edge-controller", options );
        }
    }

    private static void getCmdSetProperty(CommandLine line,String what,String defaultValue) {
        if( line.hasOption( what ) ) {
            System.setProperty(what, line.getOptionValue(what));
        } else if ( defaultValue != null) {
            System.setProperty(what,defaultValue);
        }
    }

}
