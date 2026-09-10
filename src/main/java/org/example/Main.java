package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Publisher;
import org.example.util.JpaUtil;

import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {
        createEntities();
        selectAuthor();
        updateBook(1L, "Ucenje u pokusaju!");
        selectAuthor();
        deleteBook(1L);
        System.out.println("--------------------------------");
        selectAuthor();
    }
    private static void createEntities(){
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try{
            transaction.begin();
            Author a1 = new Author("Ivan Zigolic");
            Author a2 = new Author("Tin Ujevic");

            Publisher p1 = new Publisher("Naklada1");
            Publisher p2 = new Publisher("Naklada2");

            Book b1 = new Book("Kako ne uciti JPA i HIBERNATE");
            Book b2 = new Book("Kako pametno uciti!");
            Book b3 = new Book("Nova knjiga");

            b1.addPublisher(p1);
            a1.addBook(b1);
            b2.addPublisher(p2);
            a1.addBook(b3);
            a2.addBook(b2);
            em.persist(a1);
            em.persist(a2);
            em.persist(p1);
            em.persist(p2);
            em.persist(b1);
            em.persist(b2);
            em.persist(b3);
            transaction.commit();
        } catch (Exception e) {
            if(transaction.isActive()){
                transaction.rollback();
            }
            e.printStackTrace();
        }
        finally{
            em.close();
        }
    }
    private static void selectAuthor(){
        EntityManager em = JpaUtil.getEntityManager();
        try{
            Query query = em.createQuery("SELECT a FROM Author a");
            List<Author> authors = query.getResultList();
            for(Author a : authors){
                System.out.println("Autor " + a.getName() + " | " + a.getBooks());
            }
        }
        finally{
            em.close();
        }
    }
    public static void updateBook(Long bookId, String newTitle) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            String hql = "UPDATE Book b SET b.title = :newTitle WHERE b.id = :bookId";
            Query query = em.createQuery(hql);
            query.setParameter("newTitle", newTitle);
            query.setParameter("bookId", bookId);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public static void deleteBook(Long bookId) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            String hql = "DELETE Book b WHERE b.id = :bookId";
            Query query = em.createQuery(hql);
            query.setParameter("bookId", bookId);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
