package com.order_service.feature.shipping.service.impl;
import com.order_service.feature.shipping.dto.ShippingAddressDTO;
import com.order_service.feature.shipping.entity.ShippingAddress;
import com.order_service.feature.shipping.repository.ShippingAddressRepository;
import com.order_service.feature.shipping.repository.ShippingDistrictRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static com.order_service.common.constants.CacheConstants.GET_USER_ADDRESSES;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Testcontainers
@SpringBootTest(properties = {
        "spring.profiles.active=test"
})
@Sql("/datasets/shippingCities.sql")
class ShippingAddressServiceImplIT{

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:7.0")
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        // DB
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        // Redis
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));

    }


    @Autowired
    private ShippingAddressRepository shippingAddressRepository;

    @Autowired
    private ShippingDistrictRepository shippingDistrictRepository;


    @Autowired
    private ShippingAddressServiceImpl shippingAddressService;


    @Autowired
    private CacheManager cacheManager;


    @BeforeEach
    void setUp() {


        var district = shippingDistrictRepository.findById(1L).orElseThrow();

        ShippingAddress address1 = new ShippingAddress();
        address1.setUserId(1L);
        address1.setAddressLine1("Calle 1");
        address1.setShippingDistrict(district);
        shippingAddressRepository.save(address1);
    }

    @Test
    void shouldReturnAddressesByUserId_andCacheResult() {
        List<ShippingAddressDTO> addresses1 = shippingAddressService.getByUserId(1L);
        assertThat(addresses1).hasSize(1);

        List<ShippingAddressDTO> addresses2 = shippingAddressService.getByUserId(1L);
        assertThat(addresses2).hasSize(1);

        // Verify cache
        Cache cache = cacheManager.getCache(GET_USER_ADDRESSES);
        assertThat(cache).isNotNull();
        assertThat(cache.get(1L)).isNotNull();
    }

    @Test
    void shouldReturnEmptyListIfUserHasNoAddresses() {
        List<ShippingAddressDTO> addresses = shippingAddressService.getByUserId(999L);

        assertThat(addresses).isEmpty();
    }
}
