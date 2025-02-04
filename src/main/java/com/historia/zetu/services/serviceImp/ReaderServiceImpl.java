package com.historia.zetu.services.serviceImp;

import com.historia.zetu.Repository.ReaderRepo;
import com.historia.zetu.model.ReaderInformations;
import com.historia.zetu.services.ReaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@RequiredArgsConstructor
@Service
public class ReaderServiceImpl implements ReaderService {

    private final ReaderRepo readerRepo;

    @Override
    public void saveReader(ReaderInformations readerInf) {

        boolean readerInfos = checkExistence(readerInf.getCountry());
        if(readerInfos != true) {
            long number = readerInf.getNumber();
            readerInf.setNumber(number + 1);
            readerInf.setCountry(readerInf.getCountry());
            readerInf.setCity(readerInf.getCity());
            readerInf.setIpAddress(readerInf.getIpAddress());
            readerInf.setUserAgent(readerInf.getUserAgent());
            readerRepo.save(readerInf);
        }
        readerInf.setNumber(readerInf.getNumber() + 1);
        readerRepo.save(readerInf);
    }

    @Override
    public ReaderInformations getReaderById(Long readerId) {
        return null;
    }

    @Override
    public ReaderInformations getLastReader() {
        return null;
    }

    @Override
    public boolean updateReader(ReaderInformations readerInformations) {
        return false;
    }

    @Override
    public boolean deleteReader(Long readerId) {
        return false;
    }

    private boolean checkExistence(String countryName){
        return readerRepo.existsByCountry(countryName);
    }
}
