package org.bjason.microservices.services;

import org.bjason.microservices.tickets.TicketControlServer;
import org.bjason.microservices.users.UsersControlServer;


public class Main {

    public static void main(String[] args) {

        String serverName = "NO-VALUE";

        switch (args.length) {
            case 5:
                System.setProperty("server.port", args[4]);
            case 4:
                System.setProperty("eureka.instance.url",args[0] );
                System.setProperty("redishost", args[1]);
                System.setProperty("redisport", args[2]);
                serverName = args[3].toLowerCase();
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
        System.out.println("Usage: java -jar ... <eureka url> <redis.host> <redis.port> <server-name> [server-port]");
        System.out.println(
                "     where server-name is 'user-control' or 'ticket-control' and server-port > 1024");
    }
}
