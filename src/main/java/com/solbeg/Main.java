package com.solbeg;


import com.solbeg.model.User;
import com.solbeg.model.UserBankAccount;
import com.solbeg.service.UserBankAccountProvider;
import com.solbeg.service.UserProvider;
import com.solbeg.service.UserService;
import com.solbeg.service.Users;

import java.math.BigDecimal;
import java.util.*;

public class Main {

    /**
     * Creates an instance of Optional<String> using a text parameter
     *
     * @param text - can be null
     * @return optional object that holds text
     */
    public static Optional<String> optionalOfString(String text) {
       return Optional.ofNullable(text);
    }

    /**
     * Adds a provided amount of money to the balance of an optional user.
     *
     * @param userProvider
     * @param amount       money to deposit
     */
    public static void deposit(UserProvider userProvider, BigDecimal amount) {

        userProvider.getUser().ifPresent(x->x.setBalance(x.getBalance().add(amount)));
    }

    /**
     * Creates an instance of Optional<User> using a user parameter
     *
     * @param user
     * @return optional object that holds user
     */
    public static Optional<User> optionalOfUser(User user) {
        return Optional.ofNullable(user);
    }

    /**
     * Returns the User got from UserProvider. If user provider does not provide the user,
     * returns a defaultUser
     *
     * @param userProvider
     * @param defaultUser
     * @return user from provider or defaultUser
     */
    public static User getUser(UserProvider userProvider, User defaultUser) {
        return userProvider.getUser().orElse(defaultUser);
    }

    /**
     * Passes user to {@link UserService#processUser(User)} when user is provided.
     * Otherwise calls {@link UserService#processWithNoUser()}
     *
     * @param userProvider
     * @param userService
     */
    public static void processUser(UserProvider userProvider, UserService userService) {
        if (userProvider.getUser().isPresent()) {
            userService.processUser(userProvider.getUser().get());
        } else {
            userService.processWithNoUser();
        }
    }

    /**
     * Returns user provided by {@link UserProvider}. If no user is provided it generates one using {@link Users}
     * Please note that additional user should not be generated if {@link UserProvider} returned one.
     *
     * @param userProvider
     * @return provided or generated user
     */
    public static User getOrGenerateUser(UserProvider userProvider) {
        Optional<User> optionalUser = userProvider.getUser();
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            return Users.generateUser();
        }
    }

    /**
     * Retrieves a {@link BigDecimal} balance using null-safe mappings.
     *
     * @param userProvider
     * @return optional balance
     */
    public static Optional<BigDecimal> retrieveBalance(UserProvider userProvider) {
        Optional<User> optionalUser = userProvider.getUser();
        return optionalUser.map(User::getBalance);
    }

    /**
     * Returns an {@link User} provided by {@link UserProvider}. If no user is provided,
     * throws {@link RuntimeException} with a message "No User provided!"
     *
     * @param userProvider
     * @return provided user
     */
    public static User getUser(UserProvider userProvider) {
        return  userProvider.getUser().orElseThrow(() -> new RuntimeException("No User provided!"));
    }

    /**
     * Retrieves a {@link BigDecimal} credit balance using null-safe mappings.
     *
     * @param userBankAccountProvider
     * @return optional credit balance
     */
    public static Optional<BigDecimal> retrieveCreditBalance(UserBankAccountProvider userBankAccountProvider) {
        return userBankAccountProvider.getUserBankAccount().map(UserBankAccount::getCreditBalance).get();
    }


    /**
     * @param userProvider
     * @return optional User which email ends with "@gmail.com"
     */
    public static Optional<User> retrieveUserGmail(UserProvider userProvider) {
        return userProvider.getUser().filter(user -> user.getEmail().endsWith("@gmail.com"));
    }

    /**
     * Retrieves user using  UserProvider and fallbackProvider. In case main provider does not provide the
     * User then user should be retrieved from fallbackProvider. In case both provider returns no user
     * then {@link java.util.NoSuchElementException} should be thrown.
     *
     * @param userProvider
     * @param fallbackProvider
     * @return user got from either userProvider or fallbackProvider
     */
    public static User getUserWithFallback(UserProvider userProvider, UserProvider fallbackProvider) {
        Optional<User> optionalUser = userProvider.getUser();
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            Optional<User> fallbackUser = fallbackProvider.getUser();
            if (fallbackUser.isPresent()) {
                return fallbackUser.get();

            } else {
                throw new NoSuchElementException("No User provided by both providers!");
            }
        }
    }

    /**
     * Returns a User with the highest balance. Throws {@link java.util.NoSuchElementException} if input
     * list is empty
     *
     * @param users
     * @return user with the highest balance
     */
    public static User getUserWithMaxBalance(List<User> users) {
        return users.stream()
                .max(Comparator.comparing(User::getBalance))
                .orElseThrow(() -> new NoSuchElementException("Input list is empty!"));
    }

    /**
     * Returns the lowest balance values or empty if users list is empty
     *
     * @param users
     * @return the lowest balance values
     */
    public static OptionalDouble findMinBalanceValue(List<User> users) {

               return users.stream().mapToDouble(user -> user.getBalance().doubleValue()).min();
    }


    /**
     * Calculates a sum of {@link UserBankAccount#getCreditBalance()} of all users
     *
     * @param bankAccounts
     * @return total credit balance
     */
    public static double calculateTotalCreditBalance(List<UserBankAccount> bankAccounts) {
        return bankAccounts.stream().mapToDouble(account->account.getCreditBalance().get().doubleValue()).sum();
    }
}
