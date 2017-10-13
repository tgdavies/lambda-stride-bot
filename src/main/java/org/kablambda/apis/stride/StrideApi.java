package org.kablambda.apis.stride;

import com.google.api.client.http.HttpResponse;
import org.kablambda.apis.stride.messages.Message;

import java.util.Optional;

public interface StrideApi {
    HttpResponse sendToConversation(ConversationId conversationId, Message message);

    HttpResponse conversationList(Optional<String> query, SortOrder sort, boolean includeArchived, boolean includePrivate, int limit, Optional<String> cursor);
}
