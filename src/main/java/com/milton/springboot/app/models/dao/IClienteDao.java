package com.milton.springboot.app.models.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.milton.springboot.app.models.entity.Cliente;

public interface IClienteDao extends PagingAndSortingRepository<Cliente, Long> {

}
