package org.kablambda.forecast;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.kablambda.apis.document.ApplicationCard;
import org.kablambda.apis.stride.SortOrder;
import org.kablambda.apis.stride.StrideApi;
import org.kablambda.apis.stride.messages.ChatMessageSent;
import org.kablambda.framework.Services;
import org.kablambda.framework.App;
import org.kablambda.apis.document.Doc;
import org.kablambda.apis.document.Paragraph;
import org.kablambda.apis.document.Text;
import org.kablambda.apis.stride.messages.Message;
import org.kablambda.framework.modules.Bot;
import org.kablambda.framework.modules.BotMessage;
import org.kablambda.framework.modules.MessageAction;
import org.kablambda.framework.modules.Module;

public class ForecastApp implements App {
    public List<Module> getModules() {
        return Lists.newArrayList(
                Bot.create("forecast", (api, p) -> {
                    String address = p.getMessage().getText().replace("@forecast", "");
                    if (address.contains("test")) {
                        String msg = doTest(api);
                        Services.log(msg);
                        sendMessage(api, p, msg);
                        return null;
                    }
                    if (address.replaceAll("\\s+", "").length() > 0) {
                        try {
                            HttpResponse response = geocodeAddress(address);
                            String messageText;
                            if (response.isSuccessStatusCode()) {
                                JsonElement geocodeJson = new JsonParser().parse(new InputStreamReader(response.getContent()));
                                String status = geocodeJson.getAsJsonObject().get("status").getAsString();
                                if (status.equals("ZERO_RESULTS")) {
                                    messageText = "I couldn't find the address '" + address + "'";
                                } else if (!status.equals("OK")) {
                                    messageText = "Something went wrong looking for the address '" + address + "', status is " + status;
                                } else {
                                    JsonObject firstResult = geocodeJson
                                            .getAsJsonObject().getAsJsonArray("results").get(0).getAsJsonObject();
                                    JsonObject location = firstResult.getAsJsonObject("geometry")
                                                                     .getAsJsonObject("location");
                                    String formattedAddress = firstResult.get("formatted_address").getAsString();
                                    double lat = Double.parseDouble(location.get("lat").getAsString());
                                    double longitude = Double.parseDouble(location.get("lng").getAsString());
                                    HttpResponse forecastResponse = forecast(lat, longitude);
                                    if (forecastResponse.isSuccessStatusCode()) {
                                        JsonElement forecastJson = new JsonParser().parse(new InputStreamReader(
                                                forecastResponse
                                                        .getContent()));
                                        String summary = forecastJson.getAsJsonObject()
                                                                     .get("hourly")
                                                                     .getAsJsonObject()
                                                                     .get("summary")
                                                                     .getAsString();
                                        messageText = "The forecast for " + formattedAddress + " is " + summary;
                                    } else {
                                        messageText = forecastResponse.getStatusMessage();
                                    }
                                }
                            } else {
                                messageText = response.getStatusMessage();
                            }
                            sendCard(api, p, messageText);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
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
                }),
                createBotMessage());
    }

    private BotMessage createBotMessage() {
        return new BotMessage(getName(), ".*\\b(rain|wind|snow|hot|cold|weather)\\b.*", new MessageAction() {
            @Override
            public Void doAction(StrideApi api, ChatMessageSent parameter) {
                sendMessage(api,
                            parameter,
                            "It looks like you're discussing the weather. Why not ask for a @forecast?");
                return null;
            }
        });
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

    private void sendCard(StrideApi api, ChatMessageSent p, String messageText) {
        Message m = new Message(
                new Doc(
                        1, new ApplicationCard(
                                new ApplicationCard.Attrs(
                                        null,
                                        true,
                                        null,
                                        new ApplicationCard.Attrs.Description(messageText),
                                        Collections.emptyList(),
                                        null,
                                        null,
                                        messageText,
                                        null,
                                        new ApplicationCard.Attrs.Title("Weather Forecast", null)
                                )
                        )
                )
        );
        api.sendToConversation(p.getConversation().getId(), m);
    }

    private HttpResponse geocodeAddress(String address) throws IOException {
        String geocodeUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" +
                            URLEncoder.encode(address, "UTF-8") +
                            "&key=" + System.getenv("GOOGLE_GEOCODING_KEY");
        return Services.getHttpRequestFactory()
                       .buildGetRequest(new GenericUrl(geocodeUrl))
                       .setThrowExceptionOnExecuteError(false)
                       .execute();
    }

    private HttpResponse forecast(double lat, double lng) throws IOException {
        String darkSkyUrl = "https://api.darksky.net/forecast/" + System.getenv("DARK_SKY_KEY") + "/" + lat + "," + lng;
        return Services.getHttpRequestFactory()
                       .buildGetRequest(new GenericUrl(darkSkyUrl))
                       .setThrowExceptionOnExecuteError(false)
                       .execute();
    }

    private String doTest(StrideApi api) {
        HttpResponse r = api.conversationList(Optional.empty(), SortOrder.asc, false, false, 75, Optional.empty());
        try {
            return new JsonParser().parse(new InputStreamReader(r.getContent())).toString().replaceAll("\n", "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return "forecast";
    }
}
