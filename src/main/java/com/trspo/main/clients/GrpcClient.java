package com.trspo.main.clients;

import com.services.grpc.server.care.AppointmentRequest;
import com.services.grpc.server.care.AppointmentResponse;
import com.services.grpc.server.care.CareServiceGrpc;
import com.services.grpc.server.club.*;
import com.services.grpc.server.finance.FinanceServiceGrpc;
import com.services.grpc.server.finance.PaymentIdRequest;
import com.services.grpc.server.finance.PaymentResponse;
import com.services.grpc.server.finance.ProtoFinance;
import com.services.grpc.server.groom.*;
import com.services.grpc.server.horse.*;
import com.services.grpc.server.trainer.*;
import com.services.grpc.server.training.TrainingRequest;
import com.services.grpc.server.training.TrainingResponse;
import com.services.grpc.server.training.TrainingServiceGrpc;
import com.services.grpc.server.vet.*;
import com.trspo.main.FillData;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.List;
import java.util.Random;

public class GrpcClient {
    private static final FillData fillData = new FillData();
    private static final Random rand = new Random();

    public static void clientTests(String[] args) {
        System.out.print("Let`s create an equestrian center.\n");
        createClubAccount();

        System.out.print("\nThere are some clients\n");
        createClients(3);

        System.out.print("\nAfter that we are gonna create Club's horses.\n");
        //createClubHorses(4);

        System.out.print("\nWe need some grooms for taking care of our horses\n");
        createGrooms(2);

        System.out.print("\nAlso club needs vets\n");
        createVets(2);

        System.out.print("\nAnd the most important part for making money - trainers\n");
        createTrainers(3);

        System.out.print("\nOur club is becoming popular. So, here are new clients with their horses:\n");

        List<ProtoClient> clientList = getAllClients();
        for (ProtoClient client : clientList)
            System.out.print(client + "\n");

        System.out.print("\nThe first client wants to plan a training:\n");

        if (clientList.size() == 0)
            return;

        String firstClientId = clientList.get(0).getId();

        System.out.print("\nHere is a suitable horse for this client:\n");

        ProtoHorse suitableHorse;

        try {
            suitableHorse = getSuitableHorse(firstClientId);
        } catch (Exception e) {
            System.out.print("Oppps, we have not suitable horse for your status. We are so sorry. Try later.");
            return;
        }

        System.out.print(suitableHorse + "\n");

        String horseId = suitableHorse.getId();
        System.out.print(String.format("\nLet`s take a first one with '%s' id\n", horseId));


        System.out.print("\nHere is a suitable trainer for this client:\n");

        ProtoTrainer suitableTrainer;
        try {
            suitableTrainer = getSuitableTrainer(firstClientId);
        } catch (Exception e) {
            System.out.print("Oppps, we have not suitable trainers for your sport status. We are so sorry. Try later.");
            return;
        }

        System.out.print(suitableTrainer + "\n");

        String trainerId = suitableTrainer.getId();
        System.out.print(String.format("\nWe are gonna get this one with '%s' id\n", trainerId));

        System.out.print("\nTrying create a training\n");
        createTraining(firstClientId, horseId, trainerId);
        System.out.print("\nSuccessfully created\n");

        System.out.print("\nOn the training horse was injured. It's a great time to call a doctor.");
        System.out.print("\nList of our vets:\n");
        List<ProtoVet> vetsList = getAllVets();
        for (ProtoVet vet : vetsList)
            System.out.print(vet + "\n");

        int specialistNumber = rand.nextInt(vetsList.size());
        String vetId = vetsList.get(specialistNumber).getId();
        System.out.print(String.format("\nWe will get the vet with '%s' id\n", vetId));

        System.out.print("Trying to create a vet visit\n");
        createCareAppointment(horseId, vetId);

        System.out.print("\nAfter appointment - the best way is to eat something\n");
        System.out.print("\nList of our grooms:\n");
        List<ProtoGroom> groomList = getAllGrooms();
        for (ProtoGroom groom : groomList)
            System.out.print(groom + "\n");

        specialistNumber = rand.nextInt(groomList.size());
        String groomId = groomList.get(specialistNumber).getId();
        System.out.print(String.format("\nWe will get the groom with '%s' id\n", groomId));

        System.out.print("Trying to create a groom visit\n");
        createCareAppointment(horseId, groomId);

        ProtoClub club = getClub();

        String horseOwnerId = suitableHorse.getOwnerId();
        String clubId = club.getId();

        if (!horseOwnerId.equals(clubId)) {
            System.out.print("\nList of horse owner's payments:\n");
            List<ProtoFinance> payments = getClientPayments(horseOwnerId);
            for (ProtoFinance payment : payments)
                System.out.print(payment + "\n");
        }

        System.out.print("\n\nTo sum up our results:");
        int gain = club.getMoneyAmount() - club.getSeedCapital();

        if (gain > 0)
            System.out.print(String.format("\nClub earned %s$", gain));
        else
            System.out.print(String.format("\nClub lost %s$", gain));
    }


    public static List<ProtoFinance> getClientPayments(String clientId) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        FinanceServiceGrpc.FinanceServiceBlockingStub stub = FinanceServiceGrpc.newBlockingStub(channel);
        PaymentIdRequest request = PaymentIdRequest.newBuilder()
                .setId(clientId).build();

        PaymentResponse response = stub.getClientPayments(request);
        channel.shutdown();

        return response.getPaymentsList();
    }

    public static void createTraining(String clientId, String horseId, String trainerId) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        TrainingServiceGrpc.TrainingServiceBlockingStub stub = TrainingServiceGrpc.newBlockingStub(channel);
        TrainingRequest request = TrainingRequest.newBuilder()
                .setClientId(clientId)
                .setHorseId(horseId)
                .setTrainerId(trainerId)
                .build();

        TrainingResponse response = stub.addTraining(request);
        channel.shutdown();
    }

    public static ProtoClub getClub() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        ClubServiceGrpc.ClubServiceBlockingStub stub = ClubServiceGrpc.newBlockingStub(channel);
        ClubEmpty request = ClubEmpty.newBuilder().build();

        ClubResponse response = stub.getClub(request);
        channel.shutdown();

        return response.getClub();
    }

    public static ProtoHorse getSuitableHorse(String id) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();


        ClientServiceGrpc.ClientServiceBlockingStub stub = ClientServiceGrpc.newBlockingStub(channel);

        ClientIdRequest clientIdRequest = ClientIdRequest.newBuilder()
                .setId(id).build();

        ClientResponse clientResponse = stub.getClientById(clientIdRequest);

        String horsemanStatus = clientResponse.getClient(0).getHorsemanStatus();

        HorseServiceGrpc.HorseServiceBlockingStub horseStub = HorseServiceGrpc.newBlockingStub(channel);

        HorsemanRequest horsemanRequest = HorsemanRequest.newBuilder()
                .setStatus(horsemanStatus).build();

        HorseResponse response = horseStub.getSuitableHorse(horsemanRequest);

        channel.shutdown();
        return response.getHorse(0);
    }

    public static void createCareAppointment(String horseId, String specialistId) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        CareServiceGrpc.CareServiceBlockingStub stub = CareServiceGrpc.newBlockingStub(channel);
        AppointmentRequest createAppointmentRequest = AppointmentRequest.newBuilder()
                .setHorseId(horseId)
                .setSpecialistId(specialistId)
                .build();

        AppointmentResponse response = stub.createAppointment(createAppointmentRequest);
        channel.shutdown();
    }

    public static ProtoTrainer getSuitableTrainer(String id) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        ProtoClient protoClient = getClient(id);
        String clientSportCategory = protoClient.getSportCategory();

        SportCategoryRequest sportCategoryRequest = SportCategoryRequest.newBuilder()
                .setSportCategory(clientSportCategory).build();

        TrainerServiceGrpc.TrainerServiceBlockingStub trainerStub = TrainerServiceGrpc.newBlockingStub(channel);
        TrainerResponse trainerResponse = trainerStub.getSuitableTrainer(sportCategoryRequest);
        channel.shutdown();

        return trainerResponse.getTrainers(0);
    }

    public static List<ProtoClient> getAllClients() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        ClientServiceGrpc.ClientServiceBlockingStub stub = ClientServiceGrpc.newBlockingStub(channel);
        ClientEmpty clientRequest = ClientEmpty.newBuilder().build();

        ClientResponse response = stub.getClients(clientRequest);
        channel.shutdown();

        return response.getClientList();
    }

    public static ProtoClient getClient(String id) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        ClientIdRequest clientIdRequest = ClientIdRequest.newBuilder()
                .setId(id).build();

        ClientServiceGrpc.ClientServiceBlockingStub clientStub = ClientServiceGrpc.newBlockingStub(channel);
        ClientResponse clientResponse = clientStub.getClientById(clientIdRequest);

        return clientResponse.getClient(0);
    }

    public static List<ProtoVet> getAllVets() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        VetEmpty request = VetEmpty.newBuilder().build();

        VetServiceGrpc.VetServiceBlockingStub stub = VetServiceGrpc.newBlockingStub(channel);
        VetResponse response = stub.showVets(request);
        channel.shutdown();

        return response.getVetsList();
    }

    public static List<ProtoGroom> getAllGrooms() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        GroomServiceGrpc.GroomServiceBlockingStub stub = GroomServiceGrpc.newBlockingStub(channel);
        GroomEmpty emptyRequest = GroomEmpty.newBuilder().build();
        GroomResponse response = stub.showGrooms(emptyRequest);
        channel.shutdown();

        return response.getGroomsList();
    }

    public static void createClubAccount() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        ClubServiceGrpc.ClubServiceBlockingStub stub = ClubServiceGrpc.newBlockingStub(channel);

        CreateClubRequest clubRequest = CreateClubRequest.newBuilder()
                .setSeedCapital(1250).build();
        try {
            stub.addClub(clubRequest);
        } catch (Exception e) {
            channel.shutdown();
        }
    }

    public static void createClubHorses(int numberOfHorses) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        HorseServiceGrpc.HorseServiceBlockingStub stub = HorseServiceGrpc.newBlockingStub(channel);


        for (int i = 0; i < numberOfHorses; i++) {
            HorseRequest horseRequest = HorseRequest.newBuilder()
                    .setName(fillData.getHorseName())
                    .setHorsemanStatus(fillData.getHorsemanStatus().toString())
                    .setPrice(fillData.getPrice())
                    .build();

            HorseResponse response = stub.addHorse(horseRequest);
        }

        channel.shutdown();
    }

    public static void createClients(int numberOfClients) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        ClientServiceGrpc.ClientServiceBlockingStub stub = ClientServiceGrpc.newBlockingStub(channel);

        for (int i = 0; i < numberOfClients; i++) {
            ProtoClient protoClient = ProtoClient.newBuilder()
                    .setName(fillData.getPeopleName())
                    .setHorsemanStatus(fillData.getHorsemanStatus().toString())
                    .setSportCategory(fillData.getSportCategory().toString())
                    .build();

            ClientRequest clientRequest = ClientRequest.newBuilder()
                    .setHorse(protoClient)
                    .build();

            ClientResponse response = stub.addClient(clientRequest);
        }

        channel.shutdown();
    }

    public static void createTrainers(int numberOfTrainers) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        TrainerServiceGrpc.TrainerServiceBlockingStub stub = TrainerServiceGrpc.newBlockingStub(channel);

        for (int i = 0; i < numberOfTrainers; i++) {
            TrainerRequest trainerRequest = TrainerRequest.newBuilder()
                    .setName(fillData.getPeopleName())
                    .setSalary(fillData.getSalary())
                    .setSportCategory(fillData.getSportCategory().toString())
                    .setCarePrice(fillData.getConsultationPrice())
                    .build();

            TrainerResponse response = stub.addTrainer(trainerRequest);
        }

        channel.shutdown();
    }

    public static void createVets(int numberOfVets) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        VetServiceGrpc.VetServiceBlockingStub stub = VetServiceGrpc.newBlockingStub(channel);

        for (int i = 0; i < numberOfVets; i++) {
            VetRequest vetRequest = VetRequest.newBuilder()
                    .setName(fillData.getPeopleName())
                    .setSalary(fillData.getSalary())
                    .setCarePrice(fillData.getConsultationPrice())
                    .build();

            VetResponse response = stub.addVet(vetRequest);
        }
        channel.shutdown();
    }

    public static void createGrooms(int numberOfGrooms) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        GroomServiceGrpc.GroomServiceBlockingStub stub = GroomServiceGrpc.newBlockingStub(channel);

        for (int i = 0; i < numberOfGrooms; i++) {
            GroomRequest groomRequest = GroomRequest.newBuilder()
                    .setName(fillData.getPeopleName())
                    .setSalary(fillData.getSalary())
                    .setCarePrice(fillData.getConsultationPrice())
                    .build();

            GroomResponse response = stub.addGroom(groomRequest);
        }
        channel.shutdown();
    }
}
