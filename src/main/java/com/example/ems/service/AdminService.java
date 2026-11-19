package com.example.ems.service;


import com.example.ems.dto.AdminRequest;
import com.example.ems.dto.AdminResponse;

public interface AdminService {


    AdminResponse getAdmin(Long id);

    AdminResponse updateAdmin(AdminRequest adminRequest);

}
