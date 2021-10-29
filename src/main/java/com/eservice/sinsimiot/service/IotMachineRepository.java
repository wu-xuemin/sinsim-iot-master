package com.eservice.sinsimiot.service;

import com.eservice.sinsimiot.model.iot_machine.IotMachine;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IotMachineRepository extends MongoRepository<IotMachine, String> {

}
