package net.thetowncraft.townbot.api.command_handler;

public @interface CommandConfig {

    /**
     * @return The name of this command
     */
    String name();
    String[] aliases() default {};
}