package com.ecnu;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.ecnu.dao.EssayDao;
import com.ecnu.dao.UserDao;
import com.ecnu.pojo.User;
import com.ecnu.service.ESEssayService;
import com.ecnu.service.EssayService;
import com.ecnu.service.UserService;
import com.ecnu.util.MySecurityUtil;
import com.ecnu.vo.UserInfoVo;
import com.ecnu.vo.query.EssayQuery;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@SpringBootTest
class EssayProjectApplicationTests {

    @Autowired
    UserService userService;
    @Autowired
    UserDao userDao;

    @Test
    void contextLoads(){
        User user = new User(Long.valueOf(1L),"himybro","123456","aa","jackson","123@123.com","N",false);
        UserInfoVo userInfo = new UserInfoVo();
        BeanUtils.copyProperties(user,userInfo);
        String salt = MySecurityUtil.generateRandomString(); //生成一个随机字符串作为盐值
        System.out.println(salt);
    }

    @Autowired
    EssayDao essayDao;
    @Autowired
    ESEssayService esEssayService;
    @Test
    void test1(){
        esEssayService.recoverDocument(5L);
    }

    @Autowired
    EssayService essayService;

    @Autowired
    ElasticsearchClient client;

    @SneakyThrows
    @Test
    void test2(){
            SearchResponse<ESEssay> search = client.search(s -> s
                    .index("essay")
                    .source(source -> source.fetch(false)) // 设置是否回表查询，即是否只获取hit.id()而不获取hit.source()
                    .query(q -> q
                            .multiMatch(t -> t
                                    .fields("digest","title")
                                    .query("无人")
                                    .analyzer("ik_smart")
                            )
                    ), ESEssay.class);

            for (Hit<ESEssay> hit : search.hits().hits()) {
                log.info("{}, id: {}", Optional.ofNullable(hit.source()), hit.id());
            }
        for (Hit<ESEssay> hit : search.hits().hits()) {
            System.out.println(hit.source());
            System.out.println(hit.id());
        }
        List<Long> collect = search.hits().hits().stream().map(Hit::id).mapToLong(id -> Long.parseLong(id)).boxed().collect(Collectors.toList());

        System.out.println(collect);
    }

    @SneakyThrows
    @Test
    public void searchIndex(){
        EssayQuery query = new EssayQuery();
        query.setKeyword("数据库");
        SearchResponse<com.ecnu.pojo.ESEssay> search = client.search(s -> s
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
                            if (query.getConference()!=null){
                                b.must(m -> m
                                        .match(t -> t
                                                .field("conference")
                                                .query(query.getConference())
                                                .analyzer("ik_smart")
                                        )
                                );
                            }
                            if (query.getKeyword()!=null){
                                b.must(m -> m
                                        .match(t -> t
                                                .field("conference")
                                                .query(query.getConference())
                                                .analyzer("ik_smart")
                                        )
                                );
                            }
                            return b;
                        })

                ), com.ecnu.pojo.ESEssay.class);
        List<Long> collect = search.hits().hits().stream().map(Hit::id).mapToLong(id -> Long.parseLong(id)).boxed().collect(Collectors.toList());
        for (Long aLong : collect) {
            System.out.println(aLong);
        }

    }

    @Test
    public void updateDocumentTest() throws IOException {
        ESEssay essay = new ESEssay();
        essay.setDeleted(false);
        // 不更新的字段设置为null即可
        UpdateResponse<ESEssay> updateResponse = client.update(u -> u
                        .index("essay")
                        .id("14")
                        .doc(essay),
                ESEssay.class);
    }
}
