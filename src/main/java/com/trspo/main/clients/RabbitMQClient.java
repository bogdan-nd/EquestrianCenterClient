package com.trspo.main.clients;

import com.trspo.main.FillData;
import com.trspo.main.requestDTO.GroomDTO;
import com.trspo.main.requestDTO.HorseDTO;
import com.trspo.main.requestDTO.TrainerDTO;
import com.trspo.main.requestDTO.VetDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

public class RabbitMQClient {
    private static final String URL = "http://10.99.226.180:8081";
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final HttpHeaders headers = new HttpHeaders();
    private static final HttpEntity<Object> headerEntity = new HttpEntity<>(headers);
    private static final FillData fillData = new FillData();
    private static final Random rand = new Random();

    public static void createClubHorses(int numberOfHorses) {
        for (int i = 0; i < numberOfHorses; i++) {
            HorseDTO horseDTO = HorseDTO.builder()
                    .name(fillData.getHorseName())
                    .horsemanStatus(fillData.getHorsemanStatus())
                    .price(fillData.getPrice())
                    .build();

            HttpEntity<HorseDTO> newHorse = new HttpEntity<>(horseDTO, headers);
            String response = addEntity("/horses", newHorse);
            System.out.print(response+"\n");
        }
    }

    public static void createTrainers(int numberOfTrainers) {
        for (int i = 0; i < numberOfTrainers; i++) {
            TrainerDTO trainerDTO = TrainerDTO.builder()
                    .name(fillData.getPeopleName())
                    .salary(fillData.getSalary())
                    .sportCategory(fillData.getSportCategory())
                    .trainingPrice(fillData.getConsultationPrice())
                    .build();

            HttpEntity<TrainerDTO> newTrainer = new HttpEntity<>(trainerDTO, headers);
            String response = addEntity("/trainers", newTrainer);
            System.out.print(response+"\n");
        }
    }

    public static void createVets(int numberOfVets) {
        for (int i = 0; i < numberOfVets; i++) {
            VetDTO vetDTO = VetDTO.builder()
                    .name(fillData.getPeopleName())
                    .salary(fillData.getSalary())
                    .consultationPrice(fillData.getConsultationPrice())
                    .build();

            HttpEntity<VetDTO> newVet = new HttpEntity<>(vetDTO, headers);
            String response = addEntity("/vets", newVet);
            System.out.print(response+"\n");
        }
    }

    public static void createGrooms(int numberOfGrooms) {
        for (int i = 0; i < numberOfGrooms; i++) {
            GroomDTO groomDTO = GroomDTO.builder()
                    .name(fillData.getPeopleName())
                    .salary(fillData.getSalary())
                    .carePrice(fillData.getConsultationPrice())
                    .build();

            HttpEntity<GroomDTO> newGroom = new HttpEntity<>(groomDTO, headers);
            String response = addEntity("/grooms", newGroom);
            System.out.print(response+"\n");
        }
    }

    private static String addEntity(String path, Object httpEntity) {
        ResponseEntity<String> response = restTemplate.postForEntity(URL +
                path + "/rabbitmq", httpEntity, String.class);

        return response.getBody();
    }

    public void clientTests() {
        createClubHorses(2);
        createGrooms(3);
        createVets(2);
        createTrainers(3);
    }

}
