package ru.sabirzyanov.springtest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.sabirzyanov.springtest.domain.History;
import ru.sabirzyanov.springtest.repos.HistoryRepository;
import ru.sabirzyanov.springtest.repos.UserRepository;

import java.util.Date;

@Service
public class HistoryService {
    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    UserRepository userRepository;

    public Page<History> findUserAdminDate(Long userId, Long adminId, Date from, Date to, Pageable pageable) {
        if (userRepository.findById(userId).isPresent() && userRepository.findById(adminId).isPresent())
            return historyRepository.findByUserIdAndAdminIdAndDateBetween(userId, adminId, from, to, pageable);
        return null;
    }

    public Page<History> findUserDate(Long userId, Date from, Date to, Pageable pageable) {
        if (userRepository.findById(userId).isPresent())
            return historyRepository.findByUserIdAndDateBetween(userId, from, to, pageable);
        return null;
    }

    public Page<History> findAdminDate(Long adminId, Date from, Date to, Pageable pageable) {
        if (userRepository.findById(adminId).isPresent())
            return historyRepository.findByAdminIdAndDateBetween(adminId, from, to, pageable);
        return null;
    }

    public Page<History> findDate(Date from, Date to, Pageable pageable) {
        return historyRepository.findByDateBetween(from, to, pageable);
    }
}
