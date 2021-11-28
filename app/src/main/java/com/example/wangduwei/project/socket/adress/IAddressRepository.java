package com.example.wangduwei.project.socket.adress;

/**
 * <p>
 *
 * @auther : wangduwei
 * @since : 2019/9/4  15:19
 **/
public interface IAddressRepository {
    Address next();

    Address current();

    boolean hasNext();

    void update(IAddressRepository.Callback var1);

    boolean isAvailable();

    public interface Callback {
        void complete();
    }
}
