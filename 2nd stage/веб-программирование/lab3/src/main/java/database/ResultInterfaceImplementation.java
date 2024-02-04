/*
package database;

import beans.Result;
import java.util.List;

import javax.persistence.*;
import javax.enterprise.inject.Default;

@Default
public class ResultInterfaceImplementation implements ResultInterface {
    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ResultUnit");
    private final EntityManager entityManager = entityManagerFactory.createEntityManager();

    @Override
    public void save(Result result) {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            entityManager.persist(result);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    @Override
    public boolean clear() {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            entityManager.createQuery("delete from Result").executeUpdate();
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            return false;
        }
    }

    public List<Result> getAll() {
        return entityManager.createQuery("select result from Result result ORDER BY result.id", Result.class).getResultList();
    }
}
 */

package database;

import beans.Result;

import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

@Default
public class ResultInterfaceImplementation implements ResultInterface {
    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ResultUnit");
    private final EntityManager entityManager = entityManagerFactory.createEntityManager();

    private final ResultInterface proxyInstance = (ResultInterface) Proxy.newProxyInstance(
            ResultInterface.class.getClassLoader(),
            new Class[]{ResultInterface.class},
            new ResultInterfaceInvocationHandler(this)
    );

    @Override
    public void save(Result result) {
        proxyInstance.save(result);
    }

    @Override
    public boolean clear() {
        return proxyInstance.clear();
    }

    public List<Result> getAll() {
        return entityManager.createQuery("select result from Result result ORDER BY result.id", Result.class).getResultList();
    }

    private class ResultInterfaceInvocationHandler implements InvocationHandler {
        private final ResultInterface target;

        public ResultInterfaceInvocationHandler(ResultInterface target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            EntityTransaction transaction = entityManager.getTransaction();

            try {
                transaction.begin();
                Object result;
                result = method.invoke(target, args);
                transaction.commit();
                return result;
            } catch (Exception e) {
                transaction.rollback();
                return false;
            }
        }
    }
}
