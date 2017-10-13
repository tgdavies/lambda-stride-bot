package org.kablambda.framework.modules;

import org.kablambda.apis.stride.StrideApi;
import org.kablambda.apis.stride.messages.ChatMessageSent;
import org.kablambda.framework.modules.Action;

public interface DirectMessageAction extends Action<ChatMessageSent, Void> {
    @Override
    Void doAction(StrideApi api, ChatMessageSent parameters);
}
