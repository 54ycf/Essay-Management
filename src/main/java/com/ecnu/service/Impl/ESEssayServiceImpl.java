package com.ecnu.service.Impl;

import cn.hutool.core.util.StrUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ScriptField;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.ecnu.pojo.ESEssay;
import com.ecnu.service.ESEssayService;
import com.ecnu.vo.query.EssayQuery;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ESEssayServiceImpl implements ESEssayService {
    @Autowired
    ElasticsearchClient client;

    @SneakyThrows
    public void addDocument(ESEssay essay){
        essay.setDeleted(false);
        IndexResponse indexResponse = client.index(io -> io
                .index("essay")
                .id(essay.getEssayId().toString())
                .document(essay));
    }

    @SneakyThrows
    public void deleteDocument(Long essayId){
        ESEssay essay = new ESEssay();
        essay.setEssayId(essayId);
        essay.setDeleted(true);
        updateDocument(essay);
    }

    @SneakyThrows
    public void recoverDocument(Long essayId){
        ESEssay essay = new ESEssay();
        essay.setEssayId(essayId);
        essay.setDeleted(false);
        updateDocument(essay);
    }

    @SneakyThrows
    public void updateDocument(ESEssay essay){
        UpdateResponse<ESEssay> updateResponse = client.update(u -> u
                        .index("essay")
                        .id(essay.getEssayId().toString())
                        .doc(essay),
                ESEssay.class);
    }

    @SneakyThrows
    public List<Long> searchIndex(EssayQuery query){
        SearchResponse<ESEssay> search = client.search(s -> s
                .index("essay")
                .source(source -> source.fetch(false)) // 设置是否回表查询，即是否只获取hit.id()而不获取hit.source()
                .query(q -> q
                        .bool(b -> {
                                b.must(m -> m
                                        .term(t -> t
                                            .field("deleted")
                                            .value(false)
                                        )
                                );
                                if (!StrUtil.isBlank(query.getConference())){
                                    b.should(m -> m
                                        .match(t -> t
                                            .field("conference")
                                            .query(query.getConference())
                                            .analyzer("ik_smart")
                                        )
                                    );
                                }
                                if (!StrUtil.isBlank(query.getKeyword())){
                                    b.should(m -> m
                                        .multiMatch(t -> t
                                            .fields("digest","title")
                                            .query(query.getKeyword())
                                            .analyzer("ik_smart")
                                        )
                                    );
                                }
                            return b;
                        })

                ), ESEssay.class);
        return  search.hits().hits().stream().map(Hit::id).mapToLong(id -> Long.parseLong(id)).boxed().collect(Collectors.toList());

    }

    @SneakyThrows
    public List<Map<String, Object>> searchTitles(String keyword){
        SearchResponse<ESEssay> search = client.search(s -> s
                .index("essay")
                .source(source -> source.fetch(true))
                .query(q -> q
                        .match(t -> t
                                .field("title")
                                .query(keyword)
                                .analyzer("ik_smart")
                        )
                ), ESEssay.class);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Hit<ESEssay> hit : search.hits().hits()) {
            Map<String, Object> map = new HashMap<>();
            map.put("label", hit.source().getTitle());
            map.put("value", Long.valueOf(hit.id()));
            result.add(map);
        }
        return  result;
    }

    @SneakyThrows
    public ESEssay get(String essayId) {
        GetResponse<ESEssay> getResponse = client.get(g -> g
                        .index("essay")
                        .id(essayId),
                ESEssay.class
        );
        return getResponse.source();
    }
}
