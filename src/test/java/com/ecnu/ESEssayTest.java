package com.ecnu;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.BooleanProperty;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@SpringBootTest(classes = EssayProjectApplication.class)
public class ESEssayTest {

    @Autowired
    ElasticsearchClient client;

    @Test
    public void createIndex() throws IOException {
        BooleanResponse booleanResponse = client.indices().exists(e -> e.index("essay"));
        if (!booleanResponse.value()) {
            CreateIndexResponse indexResponse = client
                    .indices()
                    .create(c -> c
                            .index("essay")
                            .mappings(m -> m
                                    .properties("digest", p -> p.text(t -> t.analyzer("ik_smart")))
                                    .properties("title", p -> p.text(t -> t.analyzer("ik_smart")))
                                    .properties("conference", p -> p.text(t -> t.analyzer("ik_smart")))
                                    .properties("deleted", p -> p.boolean_(v -> v))
                            )
                    );
        }
    }

    @Test
    public void deleteIndex() throws IOException {
        DeleteIndexResponse deleteIndexResponse = client.indices().delete(e -> e.index("essay"));
    }

    @Test
    public void get() throws IOException {
        GetResponse<ESEssay> getResponse = client.get(g -> g
                .index("essay")
                .id("1"),
                ESEssay.class
        );
        System.out.println(getResponse.source());
    }

    @Test
    public void createDocument() throws IOException {
        String[] digest = {"????????????????????????", "?????????????????????", "??????????????????", "??????????????????????????????????????????"};
        ESEssay essay = new ESEssay();
        essay.setConference("?????????????????????????????????");
        essay.setAuthor("?????????");
        essay.setTitle("??????????????????????????????????????????NOIRE???BLANC??????");
        essay.setDigest("?????????????????????????????????????????????");
        for (int i = 0; i < digest.length; i++) {
            essay.setEssayId((long) i);
            essay.setDigest(digest[i]);
            IndexResponse indexResponse = client.index(io -> io
                    .index("essay")
                    .id(essay.getEssayId().toString())
                    .document(essay));
        }

    }

    @Test
    public void createDocumentBatch() throws IOException {
        List<BulkOperation> bulkOperationArrayList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ESEssay essay = new ESEssay();
            essay.setEssayId((long) i);
            essay.setDigest("Digest " + i);
            essay.setTitle("Title " + i);
            essay.setAuthor("Author " + i);
            bulkOperationArrayList.add(
                    BulkOperation.of(o -> o
                            .index(io -> io
                                    .id(essay.getEssayId().toString())
                                    .document(essay))));
        }
        BulkResponse bulkResponse = client.bulk(b -> b.index("essay").operations(bulkOperationArrayList));
    }

    @Test
    public void updateDocumentTest() throws IOException {
        ESEssay essay = new ESEssay();
        essay.setAuthor("?????????");
        essay.setDigest("??????????????????");
        // ???????????????????????????null??????
        UpdateResponse<ESEssay> updateResponse = client.update(u -> u
                .index("essay")
                .id("1")
                .doc(essay),
                ESEssay.class);
    }


    @Test
    public void deleteDocumentTest() throws IOException {
        DeleteResponse deleteResponse = client.delete(d -> d
                .index("essay")
                .id("1")
        );
        System.out.println(deleteResponse.id());
    }

    @Test
    public void search() throws IOException {
        SearchResponse<ESEssay> search = client.search(s -> s
                .index("essay")
                .source(source -> source.fetch(false)) // ?????????????????????????????????????????????hit.id()????????????hit.source()
                .query(q -> q
                        .multiMatch(t -> t
                                .fields("digest","title")
                                .query("?????????")
                                .analyzer("ik_smart")
                        )
                ), ESEssay.class);

        for (Hit<ESEssay> hit : search.hits().hits()) {
            log.info("{}, id: {}", Optional.ofNullable(hit.source()), hit.id());
        }
    }

}
