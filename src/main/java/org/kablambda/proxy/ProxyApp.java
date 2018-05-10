package org.kablambda.proxy;

import java.util.List;

import com.google.common.collect.Lists;

import org.kablambda.apis.stride.StrideApi;
import org.kablambda.apis.stride.messages.ChatMessageSent;
import org.kablambda.aws.handler.RegisterHandler;
import org.kablambda.framework.App;
import org.kablambda.apis.document.Doc;
import org.kablambda.apis.document.Paragraph;
import org.kablambda.apis.document.Text;
import org.kablambda.apis.stride.messages.Message;
import org.kablambda.framework.modules.Bot;
import org.kablambda.framework.modules.Module;


import static org.kablambda.framework.Services.getTenant;

public class ProxyApp implements App {
    public List<Module> getModules(String tenantUuid) {
        final RegisterHandler.Register tenant = getTenant(tenantUuid);
        return Lists.newArrayList(
                Bot.create(tenant.getBotName(), (api, p) -> {
                    sendMessage(api, p, p.getMessage().getText().toUpperCase());
                    return null;
                }, (api, p) -> {
                    api.sendToConversation(
                            p.getConversation().getId(),
                            new Message(
                                    new Doc(
                                            1,
                                            Lists.newArrayList(
                                                    new Paragraph(
                                                            Lists.newArrayList(
                                                                    new Text(p.getMessage().getText().toUpperCase())
                                                            )
                                                    )
                                            )
                                    )
                            ));
                    return null;
                }));
    }


    private void sendMessage(StrideApi api, ChatMessageSent p, String messageText) {
        Message m = new Message(
                new Doc(1, Lists.newArrayList(
                        new Paragraph(Lists.newArrayList(
                                new Text(messageText)
                        ))
                ))
        );
        api.sendToConversation(p.getConversation().getId(), m);
    }

    public String getName(String tenantUuid) {
        return getTenant(tenantUuid).getBotName();
    }
}
