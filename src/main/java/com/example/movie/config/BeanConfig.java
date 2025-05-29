package com.example.movie.config;


import com.example.movie.repository.ReviewsRepository;
import com.example.movie.entity.ReviewsEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dozermapper.core.DozerBeanMapper;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.web.client.RestTemplate;
import javax.sql.DataSource;
//a
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

/*    @Bean("reviewsRepository")
    public ReviewsRepository reviewsRepository(EntityManager entityManager) {
        JpaRepositoryFactoryBean<ReviewsRepository, ReviewsEntity, Long> factory =
                new JpaRepositoryFactoryBean<>(ReviewsRepository.class);
        factory.setEntityManager(entityManager);
        return factory.getObject();
    }*/
}

