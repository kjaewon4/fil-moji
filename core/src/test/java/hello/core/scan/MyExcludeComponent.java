package hello.core.scan;

import java.lang.annotation.*;


// 얘가 붙으면 컴포넌트 스캔에서 제외
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyExcludeComponent {
}
