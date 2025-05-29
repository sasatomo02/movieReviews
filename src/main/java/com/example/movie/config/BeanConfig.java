package com.example.movie.config;


import com.example.movie.repository.ReviewsRepository;
import com.example.movie.entity.ReviewsEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dozermapper.core.DozerBeanMapper;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class BeanConfig {
    private final EntityManager entityManager;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper mapper(){
        return new ObjectMapper();
    }

    @Bean
    public DozerBeanMapper dozerMapper() {
        return (DozerBeanMapper) DozerBeanMapperBuilder.buildDefault();
    }

    @Bean("reviewsRepository") // Beanの名前を明示的に "reviewsRepository" にする
    public ReviewsRepository reviewsRepository() {
        JpaRepositoryFactoryBean<ReviewsRepository, ReviewsEntity, Long> factory =
                new JpaRepositoryFactoryBean<>(ReviewsRepository.class);
        factory.setEntityManager(entityManager);
        return factory.getObject();
    }
}

