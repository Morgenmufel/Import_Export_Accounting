package renatius.import_export_accounting.Configurations.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PartnerDetailsService partnerDetailsService;
    private final EmployeeDetailsService employeeDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/employee/**")
                .authorizeHttpRequests(auth -> auth
                        //TODO допилить все маппинги, logout
                        .requestMatchers("/employee/login", "/employee/register", "/employee/registerEmployee").permitAll()
                        .requestMatchers("/employee/warehouse/home", "/employee/warehouse/addPage", "/employee/warehouse/add", "/employee/profile", "/employee/warehouse/**").hasAuthority("ROLE_WAREHOUSE_MANAGER")
                        .requestMatchers("/employee/admin").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/employee/accountant").hasAuthority("ROLE_ACCOUNTANT")
                        .requestMatchers("/employee/warehouseManager").hasAuthority("ROLE_WAREHOUSE_MANAGER")
                        .requestMatchers("/employee/economyManager").hasAuthority("ROLE_ECONOMY_MANAGER")

                )
                .formLogin(form -> form
                        .loginPage("/employee/login")
                        .passwordParameter("passwordHash")
                        .defaultSuccessUrl("/employee/warehouse/home", true)
                        .loginProcessingUrl("/employee/login")
                        .failureUrl("/employee/login?error=true")
                        .permitAll()
                )
                .userDetailsService(employeeDetailsService);
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain2(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/partner/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/partner/login", "/partner/register", "/partner/registerPartner").permitAll()
                                .requestMatchers("/partner/homePage").hasAuthority("ROLE_PARTNER")
                        //TODO допилить все маппинги, logout
                )
                .formLogin(form -> form
                        .loginPage("/partner/login")
                        .usernameParameter("email")
                        .passwordParameter("passwordHash")
                        .defaultSuccessUrl("/partner/homePage", true)
                        .loginProcessingUrl("/partner/login")
                        .failureUrl("/partner/login?error=true")
                        .permitAll()
                )
                .userDetailsService(partnerDetailsService);
        return http.build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain commonFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/public/**", "/home").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/access-denied")
                );

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(
                "/css/**", "/js/**", "/images/**", "/webjars/**"
        );
    }
}
