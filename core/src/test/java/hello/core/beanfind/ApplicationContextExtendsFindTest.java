package hello.core.beanfind;

import hello.core.AppConfig;
import hello.core.discount.Discountpolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ApplicationContextExtendsFindTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);

    @Test
    @DisplayName("부모 타입으로 조회, 자식이 둘 이상 있으면 중복 오류가 발생한다")
    void findBeanByParentTypeDuplicate() {
        assertThrows(NoUniqueBeanDefinitionException.class,
                () -> ac.getBean(Discountpolicy.class));
    }

    @Test
    @DisplayName("부모 타입으로 조회, 자식이 둘 이상 있으면, 빈 이름을 지정하면 된다")
    void findBeanByParentTypeBeanName() {
        Discountpolicy discountpolicy = ac.getBean("rateDiscountpolicy", Discountpolicy.class);
        assertThat(discountpolicy).isInstanceOf(RateDiscountPolicy.class);
    }

    @Test
    @DisplayName("특정 타입으로 조회 (안좋은 방법)")
    void findBeanBySubType() {
        RateDiscountPolicy bean = ac.getBean(RateDiscountPolicy.class);
        assertThat(bean).isInstanceOf(RateDiscountPolicy.class);
    }

    @Test
    @DisplayName("부모 타입으로 모두 조회하기")
    void findBeanByParentType() {
        Map<String, Discountpolicy> beansOfType = ac.getBeansOfType(Discountpolicy.class);
        assertThat(beansOfType.size()).isEqualTo(2);

        for (String key : beansOfType.keySet()) {
            System.out.println("key: " + key + ", bean: " + beansOfType.get(key));
        }
    }

    @Test
    @DisplayName("부모 타입으로 모두 조회하기 - Object")
    void findBeanByParentTypeObject() {
        Map<String, Object> beansOfType = ac.getBeansOfType(Object.class);

        // 실제 테스트에서는 출력물을 만들면 안됨
        for (String key : beansOfType.keySet()) {
            System.out.println("key: " + key + ", bean: " + beansOfType.get(key));
        }
    }

    @Configuration
    static class TestConfig {

        @Bean
        public Discountpolicy rateDiscountpolicy() {
            return new RateDiscountPolicy();
        }

        @Bean
        public Discountpolicy fixDiscountpolicy() {
            return new FixDiscountPolicy();
        }

    }
}
