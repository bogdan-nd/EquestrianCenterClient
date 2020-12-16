package com.trspo.main.clients;

import com.trspo.main.FillData;
import com.trspo.main.enums.HorsemanStatus;
import com.trspo.main.requestDTO.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import com.trspo.main.entities.*;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

public class RestClient {
    private static final String URL = "http://localhost:8081";
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final HttpHeaders headers = new HttpHeaders();
    private static final HttpEntity<Object> headerEntity = new HttpEntity<>(headers);
    private static final FillData fillData = new FillData();
    private static final Random rand = new Random();

    public static void clientTests(String[] args){
        headers.setContentType(MediaType.APPLICATION_JSON);
        System.out.print("Let`s create an equestrian center.\n");

        try {
            createClubAccount();
        }
        catch (Exception e){
            System.out.print("Huh, it already exists\n");
        }

        System.out.print("\nAfter that we are gonna create Club's horses.\n");
        createClubHorses(4);

        System.out.print("\nWe need some grooms for taking care of our horses\n");
        createGrooms(2);

        System.out.print("\nAlso club needs vets\n");
        createVets(2);

        System.out.print("\nAnd the most important part for making money - trainers\n");
        createTrainers(3);

        System.out.print("\nOur club is becoming popular. So, here are new clients with their horses:\n");
        createClientsWithHorses(2);

        Client[] cliensList = getAllClients();
        for(Client client:cliensList)
            System.out.print(client + "\n");

        System.out.print("\nThe first client wants to plan a training:\n");
        UUID firstClientId = cliensList[0].getId();

        System.out.print("\nHere is a suitable horse for this client:\n");

        Horse suitableHorse;

        try {
            suitableHorse = getSuitableHorse(firstClientId);
        }
        catch (Exception e){
            System.out.print("Oppps, we have not suitable horse for your status. We are so sorry. Try later.");
            return;
        }

        System.out.print(suitableHorse+"\n");

        UUID horseId = suitableHorse.getId();
        System.out.print(String.format("\nLet`s take a first one with '%s' id\n", horseId));


        System.out.print("\nHere is a suitable trainer for this client:\n");

        Trainer suitableTrainer;
        try{
            suitableTrainer = getSuitableTrainer(firstClientId);
        }
        catch (Exception e){
            System.out.print("Oppps, we have not suitable trainers for your sport status. We are so sorry. Try later.");
            return;
        }

        System.out.print(suitableTrainer+"\n");

        UUID trainerId = suitableTrainer.getId();
        System.out.print(String.format("\nWe are gonna get this one with '%s' id\n", trainerId));

        System.out.print("\nTrying create a training\n");
        createTraining(firstClientId, horseId, trainerId);
        System.out.print("\nSuccessfully created\n");

        System.out.print("\nOn the training horse was injured. It's a great time to call a doctor.");
        System.out.print("\nList of our vets:\n");
        Vet[] vetsList = getAllVets();
        for(Vet vet:vetsList)
            System.out.print(vet+"\n");

        int specialistNumber = rand.nextInt(vetsList.length);
        UUID vetId = vetsList[specialistNumber].getId();
        System.out.print(String.format("\nWe will get the vet with '%s' id\n",vetId));

        System.out.print("Trying to create a vet visit\n");
        createCareAppointment(horseId, vetId, "recover");

        System.out.print("\nAfter appointment - the best way is to eat something\n");
        System.out.print("\nList of our grooms:\n");
        Groom[] groomList = getAllGrooms();
        for(Groom groom:groomList)
            System.out.print(groom+"\n");

        specialistNumber = rand.nextInt(groomList.length);
        UUID groomId = groomList[specialistNumber].getId();
        System.out.print(String.format("\nWe will get the groom with '%s' id\n",groomId));

        System.out.print("Trying to create a groom visit\n");
        createCareAppointment(horseId, groomId, "feed");

        System.out.print("List of horse appointments:\n");
        Appointment[] appointments = getHorseAppointments(horseId);

        for(Appointment appointment:appointments)
            System.out.print(appointment+"\n");

        Club club = getClub();

        UUID horseOwnerId = suitableHorse.getOwnerId();
        UUID clubId = club.getId();

        if(!horseOwnerId.equals(clubId)) {
            System.out.print("\nList of horse owner's payments:\n");
            FinancePayment[] payments = getClientPayments(horseOwnerId);
            for(FinancePayment payment:payments)
                System.out.print(payment+"\n");
        }

        System.out.print("\n\nTo sum up our results:");
        int gain = club.getMoneyAmount() - club.getSeedCapital();

        if(gain>0)
            System.out.print(String.format("\nClub earned %s$",gain));
        else
            System.out.print(String.format("\nClub lost %s$",gain));
    }

    public static Appointment[] getHorseAppointments(UUID horseId){
        ResponseEntity<Appointment[]> appointmentsResponse =  restTemplate.exchange(URL + "/horses/"+horseId+"/appointments/",
                HttpMethod.GET, headerEntity, Appointment[].class);

        return appointmentsResponse.getBody();
    }

    public static FinancePayment[] getClientPayments(UUID clientId){
        ResponseEntity<FinancePayment[]> paymentResponse = restTemplate.exchange(URL + "/clients/"+clientId+"/payments/",
                HttpMethod.GET, headerEntity, FinancePayment[].class);

        return paymentResponse.getBody();
    }

    public static void createTraining(UUID clientId, UUID horseId, UUID trainerId){
        TrainingDTO trainingDTO = TrainingDTO.builder()
                .horseId(horseId)
                .clientId(clientId)
                .trainerId(trainerId)
                .startTime(LocalDateTime.of(2020,11,10,18,40))
                .build();

        HttpEntity<TrainingDTO> newTrainer = new HttpEntity<>(trainingDTO, headers);
        restTemplate.exchange(URL+ "/trainings",HttpMethod.POST, newTrainer, Training.class);
    }

    public static Club getClub(){
        ResponseEntity<Club> response = restTemplate.exchange(URL + "/club", HttpMethod.GET,
                headerEntity, Club.class);

        return response.getBody();
    }

    public static Horse getSuitableHorse(UUID id){
        ResponseEntity<Horse> response = restTemplate.exchange(URL+ String.format("/clients/%s/choose-horse", id)
                ,HttpMethod.GET, headerEntity, Horse.class);

        return response.getBody();
    }

    public static String  createCareAppointment(UUID horseId, UUID specialistId, String procedure){
        AppointmentDTO appointmentDTO = AppointmentDTO.builder()
                .horseId(horseId)
                .specialistId(specialistId)
                .build();

        HttpEntity<AppointmentDTO> care = new HttpEntity<>(appointmentDTO, headers);
        ResponseEntity<String> response = restTemplate.exchange(URL + "/appointments/" + procedure, HttpMethod.POST, care,String.class);
        return response.getBody();
    }

    public static Trainer getSuitableTrainer(UUID id){
        ResponseEntity<Trainer> response = restTemplate.exchange(URL+ String.format("/clients/%s/choose-trainer", id)
                ,HttpMethod.GET, headerEntity, Trainer.class);
        return response.getBody();
    }

    public static Client[] getAllClients(){
        ResponseEntity<Client[]> response = restTemplate.exchange(URL+"/clients", HttpMethod.GET, headerEntity, Client[].class);
        return response.getBody();
    }

    public static Client getClient(UUID id){
        ResponseEntity<Client> response = restTemplate.exchange(URL+"/clients" + id, HttpMethod.GET, headerEntity, Client.class);
        return response.getBody();
    }

    public static Vet[] getAllVets(){
        ResponseEntity<Vet[]> response = restTemplate.exchange(URL +"/vets", HttpMethod.GET, headerEntity, Vet[].class);
        return response.getBody();
    }

    public static Groom[] getAllGrooms(){
        ResponseEntity<Groom[]> response = restTemplate.exchange(URL +"/grooms", HttpMethod.GET, headerEntity, Groom[].class);
        return response.getBody();
    }

    public static void createClubAccount(){
        ClubAccountDTO clubAccountDTO = new ClubAccountDTO();
        clubAccountDTO.setSeedCapital(142000);

        HttpEntity<ClubAccountDTO> newClub = new HttpEntity<>(clubAccountDTO,headers);
        try {
            ResponseEntity<Club> response = restTemplate.exchange(URL + "/club", HttpMethod.POST, newClub, Club.class);
        }
        catch (Exception e){
            System.out.print("Opps,it's already exist");
        }
    }

    public static void createClubHorses(int numberOfHorses){
        for(int i =0; i < numberOfHorses;i++){
            HorseDTO horseDTO = HorseDTO.builder()
                    .name(fillData.getHorseName())
                    .horsemanStatus(fillData.getHorsemanStatus())
                    .price(fillData.getPrice())
                    .build();

            HttpEntity<HorseDTO> newHorse = new HttpEntity<>(horseDTO, headers);
            restTemplate.exchange(URL + "/horses", HttpMethod.POST, newHorse, Horse.class);
        }
    }

    public static void createTrainers(int numberOfTrainers){
        for(int i =0; i < numberOfTrainers;i++){
            TrainerDTO trainerDTO = TrainerDTO.builder()
                    .name(fillData.getPeopleName())
                    .salary(fillData.getSalary())
                    .sportCategory(fillData.getSportCategory())
                    .trainingPrice(fillData.getConsultationPrice())
                    .build();

            HttpEntity<TrainerDTO> newTrainer = new HttpEntity<>(trainerDTO, headers);
            restTemplate.exchange(URL + "/trainers", HttpMethod.POST, newTrainer, Trainer.class);
        }
    }

    public static void createClientsWithHorses(int numberOfClients){
        for(int i =0; i < numberOfClients;i++){
            HorsemanStatus clientHorsemanStatus = fillData.getHorsemanStatus();

            ClientDTO clientDTO = ClientDTO.builder()
                    .name(fillData.getPeopleName())
                    .horsemanStatus(clientHorsemanStatus)
                    .sportsCategory(fillData.getSportCategory())
                    .build();

            HttpEntity<ClientDTO> newClient = new HttpEntity<>(clientDTO, headers);
            ResponseEntity<Client> clientResponse = restTemplate.exchange(
                    URL + "/clients", HttpMethod.POST, newClient, Client.class);

            UUID clientId = clientResponse.getBody().getId();

            HorseDTO horseDTO = HorseDTO.builder()
                    .name(fillData.getHorseName())
                    .horsemanStatus(clientHorsemanStatus)
                    .ownerId(clientId)
                    .price(fillData.getPrice())
                    .build();

            HttpEntity<HorseDTO> newHorse = new HttpEntity<>(horseDTO, headers);
            restTemplate.exchange(URL+"horses", HttpMethod.POST,
                    newHorse, Horse.class);
        }
    }

    public static void createVets(int numberOfVets){
        for(int i =0; i < numberOfVets;i++){
            VetDTO vetDTO = VetDTO.builder()
                    .name(fillData.getPeopleName())
                    .salary(fillData.getSalary())
                    .consultationPrice(fillData.getConsultationPrice())
                    .build();

            HttpEntity<VetDTO> newVet = new HttpEntity<>(vetDTO, headers);
            restTemplate.exchange(URL + "/vets", HttpMethod.POST, newVet, Vet.class);
        }
    }

    public static void createGrooms(int numberOfGrooms){
        for(int i =0; i < numberOfGrooms;i++){
            GroomDTO groomDTO = GroomDTO.builder()
                    .name(fillData.getPeopleName())
                    .salary(fillData.getSalary())
                    .carePrice(fillData.getConsultationPrice())
                    .build();

            HttpEntity<GroomDTO> newGroom = new HttpEntity<>(groomDTO, headers);
            ResponseEntity<Groom> response = restTemplate.exchange(
                    URL + "/grooms", HttpMethod.POST, newGroom, Groom.class);
            System.out.print(response.getBody());
        }
    }
}
