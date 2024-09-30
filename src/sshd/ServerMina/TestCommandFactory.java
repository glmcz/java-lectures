package sshd.ServerMina;

import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.command.CommandFactory;

import java.io.IOException;

public class TestCommandFactory implements CommandFactory {
    @Override
    public Command createCommand(ChannelSession channelSession, String s) throws IOException {
        return new TestCommand();
    }
}


