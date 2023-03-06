package ec.springboot.app.models.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import ec.springboot.app.models.entity.Cliente;

//Aqui se puede extender de un Crud respository instanciando a cliente y el tipo de dato

public interface IClienteDao extends JpaRepository<Cliente, Long> {

}