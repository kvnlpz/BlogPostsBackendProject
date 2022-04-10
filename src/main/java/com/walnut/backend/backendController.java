package com.walnut.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api")
@EnableAsync
public class backendController {
    Map<String, String> jsonCache = new ConcurrentHashMap<>();
    ObjectMapper objectMapper;
    ObjectNode objectNode;
    Set<String> jsonSet = ConcurrentHashMap.newKeySet();

    @RequestMapping(value = "/ping", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity getPing() {
        objectMapper = new ObjectMapper();
        objectNode = objectMapper.createObjectNode();
        objectNode.put("success", "true");
        return ResponseEntity.ok(objectNode);
    }

    @RequestMapping(path = "/posts", method = RequestMethod.GET, produces = "application/json")
    @ResponseStatus
    public ResponseEntity getPosts(@RequestParam(value = "tags", required = false) Optional<String> tags,
                                   @RequestParam(value = "sortBy", required = false, defaultValue = "id") Optional<String> sortBy,
                                   @RequestParam(value = "direction", required = false, defaultValue = "desc") Optional<String> direction)
            throws IOException, JSONException {

        //check if the call contained the tag before making calls(s)
        if (!tags.isPresent()) {
            objectMapper = new ObjectMapper();
            objectNode = objectMapper.createObjectNode();
            objectNode.put("error", "Tags parameter is required");
            return ResponseEntity.badRequest().body(objectNode);
        }

        //check if the direction we got is either asc or desc ONLY
        if (!direction.get().equals("asc") && !direction.get().equals("desc")) {
            objectMapper = new ObjectMapper();
            objectNode = objectMapper.createObjectNode();
            objectNode.put("error", "Direction parameter is incorrect");
            return ResponseEntity.badRequest().body(objectNode);
        }

        //check if the sortBy value is correct before even making the call
        String sort = sortBy.get();
        if (!(sort.equals("id") || sort.equals("reads") || sort.equals("likes") || sort.equals("popularity"))) {
            objectMapper = new ObjectMapper();
            objectNode = objectMapper.createObjectNode();
            objectNode.put("error", "sortBy parameter is invalid");
            return ResponseEntity.badRequest().body(objectNode);
        }

        //Since everything has been verified to be correct, make the call(s)
        //splitting up the tags because the API can only use one tag at a time
        String[] aTags = tags.get().split(",");
        String Url = "https://api.hatchways.io/assessment/blog/posts?tag=";

        //using an array of CompletableFuture objects for our concurrent calls to be stored
        CompletableFuture<String>[] results = new CompletableFuture[aTags.length];
        for (int i = 0; i < aTags.length; i++) {
            CompletableFuture<String> jsonFuture = getJsonsAsync(new URL(Url + aTags[i]));
            results[i] = jsonFuture;
        }

        //we don't need to do anything else with this CompletableFuture because inside
        //our getJsonsAsync() method, we already add the posts to our concurrent set
        CompletableFuture.allOf(results).join();

        //after getting all the posts we just need to return a sorted version of them
        return ResponseEntity.ok(sortJson(jsonSet, sort, direction.get()).toString());
    }

    public CompletableFuture<String> getJsonsAsync(final URL url) throws IOException {
        String urlString = url.toString();
        if (jsonCache.containsKey(urlString)) return CompletableFuture.completedFuture(jsonCache.get(urlString));
        else {
            try (InputStream input = url.openStream()) {
                String jsonString = IOUtils.toString(input, StandardCharsets.UTF_8);
                jsonCache.put(urlString, jsonString);
                jsonSet.add(jsonString);
                return CompletableFuture.completedFuture(IOUtils.toString(input, StandardCharsets.UTF_8));
            }
        }
    }


    //method used to sort the output based on user-inputted criterion
    private JSONObject sortJson(Set<String> jsonStrings, String criterion, String ordering)
            throws JsonProcessingException, JSONException {

        //method scope Set used here since we only want it if we're actually using it
        //the set is to avoid duplicate posts which come from different "tag" outputs
        //i.e: posts that have multiple tags don't appear twice [tech, history]
        Set<JsonNode> setJson = new HashSet<>();
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);

        for (String jsonString : jsonStrings) {
            JsonNode node = objectMapper.readTree(jsonString);
            ArrayNode arrayNode = (ArrayNode) node.get("posts");
            Iterator<JsonNode> iterator = arrayNode.elements();
            //converting every json string's respective elements to json nodes
            //this is so that we can sort them, serialization
            while (iterator.hasNext()) {
                JsonNode jsonNode = iterator.next();
                setJson.add(jsonNode);
            }
        }

        List<JsonNode> list = new ArrayList<>(setJson);
        int order = (ordering.equals("desc") ? -1 : 1);
        list.sort((o1, o2) -> {
            //since the API solution output seemed to sort based off ID instead of the actual criterion when
            //the objects we're "equal" I made the fall-back approach
            //sort by ID if both criterion values are equal
            double a = Double.parseDouble(o1.get(criterion).asText());
            double b = Double.parseDouble(o2.get(criterion).asText());
            double compared = Double.compare(a, b);
            if (compared == 0.0) {
                return order * Double.compare(Double.parseDouble(o1.get("id").asText()), Double.parseDouble(o2.get("id").asText()));
            }
            return (int) (order * compared);
        });

        JSONArray jsonArray = new JSONArray();
        for (JsonNode jsonNode : list) jsonArray.put(new JSONObject(jsonNode.toString()));
        return new JSONObject().put("posts", jsonArray);
    }
}

