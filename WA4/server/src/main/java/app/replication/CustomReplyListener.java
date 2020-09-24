package app.replication;

import app.models.HashSignaturePair;
import app.models.QuorumReply;
import app.models.QuorumReplyList;
import bftsmart.communication.client.ReplyListener;
import bftsmart.tom.AsynchServiceProxy;
import bftsmart.tom.RequestContext;
import bftsmart.tom.core.messages.TOMMessage;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CustomReplyListener implements ReplyListener {
    private AsyncRequestHandler<QuorumReplyList> handler;
    private AsynchServiceProxy proxy;
    private List<QuorumReply> replies;

    public CustomReplyListener(AsynchServiceProxy proxy, AsyncRequestHandler<QuorumReplyList> handler) {
        this.proxy = proxy;
        this.handler = handler;
        this.replies = new LinkedList<>();
    }

    @Override
    public void reset() {
        this.replies.clear();
    }

    @Override
    public void replyReceived(RequestContext requestContext, TOMMessage tomMessage) {
        QuorumReply reply = buildQuorumReply(tomMessage.getSender(), tomMessage.getContent());
        replies.add(reply);

        if (replies.size() >= getReplyQuorum()) {
            QuorumReply result = getResult(replies);
            List<HashSignaturePair> signatures = replies.stream().filter(Objects::nonNull)
                    .map(i -> new HashSignaturePair(i.getHash(), i.getSignature(), i.getSenderId()))
                    .collect(Collectors.toList());
            QuorumReplyList replyList = new QuorumReplyList(signatures, result.getMessage());
            handler.run(replyList);
            proxy.cleanAsynchRequest(requestContext.getReqId());
        }
    }

    private QuorumReply buildQuorumReply(int id, byte[] content) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(content)) {
            try (DataInputStream dis = new DataInputStream(bis)) {
                byte[] message = new byte[dis.readInt()];
                dis.read(message);
                byte[] hash = new byte[dis.readInt()];
                dis.read(hash);
                byte[] signature = new byte[dis.readInt()];
                dis.read(signature);
                return new QuorumReply(id, message, hash, signature);
            }
        } catch (IOException e) {
            return null;
        }
    }

    private int getReplyQuorum() {
        return proxy.getViewManager().getStaticConf().isBFT() ? (int) Math.ceil((double) ((this.proxy.getViewManager().getCurrentViewN() + this.proxy.getViewManager().getCurrentViewF()) / 2)) + 1 : (int) Math.ceil((double) (this.proxy.getViewManager().getCurrentViewN() / 2)) + 1;
    }

    private QuorumReply getResult(List<QuorumReply> replies) {
        QuorumReply result = null;
        int max = -1;

        for (QuorumReply reply : replies) {
            int counter = 0;
            List<QuorumReply> aux = new LinkedList<>(replies);
            aux.remove(reply);
            for (QuorumReply auxRep : aux) {
                if (reply != null && auxRep != null && Arrays.equals(reply.getMessage(), auxRep.getMessage())) {
                    counter++;
                }
            }
            if (counter >= max) {
                max = counter;
                result = reply;
            }
        }

        return result;
    }
}