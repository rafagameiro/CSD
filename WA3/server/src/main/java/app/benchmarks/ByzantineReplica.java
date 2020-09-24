package app.benchmarks;

import bftsmart.tom.MessageContext;
import bftsmart.tom.ServiceReplica;
import bftsmart.tom.server.defaultservices.DefaultSingleRecoverable;

public class ByzantineReplica extends DefaultSingleRecoverable {

    public static void main(String[] args) {
        new ByzantineReplica(Integer.parseInt(args[0]));
    }

    private ByzantineReplica(int id) {
        new ServiceReplica(id, this, this);
    }

    @Override
    public void installSnapshot(byte[] bytes) {

    }

    @Override
    public byte[] getSnapshot() {
        return new byte[0];
    }

    @Override
    public byte[] appExecuteOrdered(byte[] bytes, MessageContext messageContext) {
        return new byte[0];
    }

    @Override
    public byte[] appExecuteUnordered(byte[] bytes, MessageContext messageContext) {
        return new byte[0];
    }
}
