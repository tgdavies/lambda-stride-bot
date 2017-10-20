package org.kablambda.framework.modules;

import org.kablambda.apis.stride.StrideApi;
import org.kablambda.apis.stride.messages.ChatMessageSent;
import org.kablambda.framework.modules.Action;

public interface MentionAction extends Action<ChatMessageSent, Void> {
}
