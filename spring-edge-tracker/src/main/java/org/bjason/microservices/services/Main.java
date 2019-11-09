package org.bjason.microservices.services;

import org.bjason.microservices.tickets.TicketControlServer;
import org.bjason.microservices.users.UsersControlServer;


public class Main {

    public static void main(String[] args) {

        String serverName = "NO-VALUE";

        switch (args.length) {
            case 2:
                // Optionally set the HTTP port to listen on, overrides
                // value in the <server-name>-server.yml file
                System.setProperty("server.port", args[1]);
                // Fall through into ..

            case 1:
                serverName = args[0].toLowerCase();
                break;

            default:
                usage();
                return;
        }

        if (serverName.equals("user-control")) {
            UsersControlServer.main(args);
        } else if (serverName.equals("ticket-control")) {
            TicketControlServer.main(args);
        } else {
            System.out.println("Unknown server type: " + serverName);
            usage();
        }
    }

    protected static void usage() {
        System.out.println("Usage: java -jar ... <server-name> [server-port]");
        System.out.println(
                "     where server-name is 'user-control' or 'ticket-control' and server-port > 1024");
    }
}
