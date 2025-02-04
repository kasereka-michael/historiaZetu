package com.historia.zetu.services;


import com.historia.zetu.model.ReaderInformations;

public interface ReaderService {


    public void saveReader(ReaderInformations readerInformations);

    public ReaderInformations getReaderById(Long readerId);

    public ReaderInformations getLastReader();

    public boolean updateReader(ReaderInformations readerInformations);

    public boolean deleteReader(Long readerId);


}
