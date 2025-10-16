package com.smarttruck;

import com.smarttruck.infrastructure.entity.UserEntity;
import com.smarttruck.infrastructure.persistence.repository.SpringDataJpaUserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@SpringBootApplication(
    scanBasePackages = {
        "com.smarttruck.*",
        //Para funcionamento da Arquitetura
        "com.smarttruck.presentation.*",
        "com.smarttruck.presentation.controller"
    }
)
@EntityScan(basePackageClasses = { Jsr310JpaConverters.class },
    basePackages = {
        "com.smarttruck.*",
        //Para funcionamento da Arquitetura
        "com.smarttruck.infrastructure.entity.*"}
)
public class SmartTruckApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartTruckApplication.class, args);
    }

    /**
     * Este Bean é executado na inicialização para popular o banco de dados.
     * Ele verifica se o usuário admin já existe. Se não existir, ele cria.
     *
     * @param userRepository O repositório real do Spring Data JPA para interagir com o banco.
     * @return Uma tarefa a ser executada.
     */
    @Bean
    CommandLineRunner initDatabase(SpringDataJpaUserRepo userRepository) {
        return args -> {
            // Verifica se o usuário já existe para não criar duplicatas
            if (userRepository.findByEmail("admin@smarttruck.com").isEmpty()) {
                System.out.println(">>> Populando banco de dados com usuário administrador...");

                UserEntity adminUser = new UserEntity();
                adminUser.setEmail("admin@smarttruck.com");
                adminUser.setPassword("123456"); // Lembre-se: em produção, isso deve ser um hash!

                userRepository.save(adminUser);

                System.out.println(">>> Usuário 'admin@smarttruck.com' criado com sucesso.");
            } else {
                System.out.println(">>> Usuário 'admin@smarttruck.com' já existe. Nenhuma ação necessária.");
            }
        };
    }
}
