# Project Assignment POO  - J. POO Morgan - Phase One

![](https://s.yimg.com/ny/api/res/1.2/aN0SfZTtLF5hLNO0wIN3gg--/YXBwaWQ9aGlnaGxhbmRlcjt3PTcwNTtoPTQyNztjZj13ZWJw/https://o.aolcdn.com/hss/storage/midas/b23d8b7f62a50a7b79152996890aa052/204855412/fit.gif)

#### Assignment Link: [https://ocw.cs.pub.ro/courses/poo-ca-cd/teme/2024/proiect-e1](https://ocw.cs.pub.ro/courses/poo-ca-cd/teme/2024/proiect-e1)

## Skel Structure

* src/
    * checker/ - checker files
    * fileio/ - contains classes used to read data from the json files
    * main/
      * dependencies/ - contains classes used to get through the assignment
        * InputParser
        * InternalActivities
        * ExchangeRate
        * Commerciant
        * commands/
           * executes/
             * transactionhelper/
               * TransactionHelper - interface for the report tasks
               * TransactionHelper children that have specific outputs of type ObjectNode
             * Command interface children, every command required by the assignment
          * Command - interface for the Command pattern
          * CommandProcessor - Processes every command from the CommandInput[]
        * userinfo/
          * Account - the Account class stores the account information based on inputs, also Account keeps information about the cards issued on its behalf, dependency of User
          * Card - the Card class stores the card information based on inputs, dependency of Account
          * User - the User class represents one user from the whole database, having information about it. User keeps information about all of its accounts.
      * Main - the Main class runs the checker on your implementation. Add the entry point to your implementation in it. Run Main to test your implementation from the IDE or from command line.
      * Test - run the main method from Test class with the name of the input file from the command line and the result will be written
              to the out.txt file. Thus, you can compare this result with ref.
* input/ - contains the tests in JSON format
* ref/ - contains all reference output for the tests in JSON format

## Tests

Tests Basic 1 - 8: Infrastructure \
Tests Functional 9 - 17: Advanced \
Tests Flow 18 - 20: Large Input

1. test01_create - 2p
2. test02_delete - 2p
3. test03_one_time_card - 2p
4. test04_funds - 2p
5. test05_money_flow - 2p
6. test06_non_existing - 2p
7. test07_send_money_part1 - 3p
8. test08_send_money_part2 - 3p
9. test09_print_transactions - 3p
10. test10_errors - 3p
11. test11_card_status - 5p
12. test12_continuous_payments - 5p
13. test13_savings_account - 5p
14. test14_split_payments - 5p
15. test15_every_payment - 5p
16. test16_report - 5p
17. test17_spendings_report - 5p
18. test18_large_input_1 - 7p
19. test19_large_input_2 - 7p
20. test19_large_input_3 - 7p


## Implementation details
### Breakthrough
* Concepts used:
    * Inheritance
    * Interface
    * Dynamic polymorphism
    * Getters/Setters
    * Command design pattern

### Implementation logic
I used a simple thought process that it's easy to debug in case I make a mistake, or I misunderstand. 
The assignment didn't provide information in certain cases, which obliged me to go through tests/refs 
to see the output. The output nodes created overall may have some if statements to add certain fields which doesn't make sense.


My implementation starts in <b><i>InputParser.java</i></b>, calling it in <b><i>Main.java</i></b>. Its purpose, as the name suggests, 
is to parse the input information into my own classes. The data gets parsed into <b><i>Internal Activities.java</i></b>
which runs each command from input by calling <b>CommandProcessor.process(commandInput)</b>.


<b>CommandProcessor</b> handles each command string by assigning one object from the HashMap.
* <i>Map<String, Command> commandClassMap</i>  - String is the key, Command is the value, in our case the value being a new Command interface child
  * The command processing starts at  
    >final Command commandClass = commandClassMap.get(commandName); \
     commandClass.execute(command, users, exchangeRates);
    * commandClass.execute(command, users, exchangeRates) runs the execute function from the Command interface child
      * Commands:
        *    printUsers         - prints database of users 
        *    printTransactions  - prints transactions of the specified user 
        *    addAccount         - adds a new bank account in the user information 
        *    addFunds           - adds funds to the specified bank account 
        *    createCard         - assignes a new card to a specified account 
        *    createOneTimeCard  - assignes a new one time card to a specified account, uses the same function, but has different type 
        *    deleteAccount      - deletes a bank account based on which user has this bank account 
        *    deleteCard         - delete a card based on its card number mentioned by the input 
        *    setMinBalance      - sets a minimum balance on a specified bank account 
        *    checkCardStatus    - checks the card status with the specified card number 
        *    payOnline          - payment (source -> commerciant) 
        *    sendMoney          - payment (sender -> receiver) 
        *    setAlias           - sets an alias to the account specified in the input 
        *    splitPayment       - payment(splitedPay -> somewhere(don't ask me)), splitedPay is converted to the payer currency so it can be deducted 
        *    addInterest        - adds interest to an account if it's a savings one 
        *    changeInterestRate - changes the interest rate of a savings account 
        *    report             - shows a report of all the transactions of a user in a specified timeframe 
        *    spendingsReport    - shows a report of an account's spendings to commerciants, solely payOnlineCommand outputs on a specified timeframe 
        \
      general explanation (for more information, check the javadoc for each Command class, or the comments in the implementation)

### How do I print in output?
 For this, we use ObjectNodes and ArrayNodes to put them into a big ArrayNode (output).
* <b>Did I handle it correctly?</b>

Not really, Due to the mix of output nodes, it is difficult to make them the same, I implemented the functions whenever I needed them, therefore I created some nodes directly into the Command functions that needed them
* <b>How can I solve the issues in the next stage of the assignment?</b>

I will review the ways it does the output and try to call TransactionHelper.printTransactions() and add it to the main ArrayNode (output).

### Final thoughts
I consider that I handled quite well the whole assignment in my own style.

Having a large directory hierarchy and each file having a small amount of code means that it's easily readable and I can find issues much faster.

What I don't like about my code is how messy I handled the output and also the <i>users</i> and <i>exchangeRates</i> should've been that I keep calling each function.

I should add users and exchangeRates + other information into a database kind of class (which I might consider to take this action sooner if it really helps the code).


## Disclaimer
I would like to mention that this implementation is not complete
due to the lack of information on most of the commands that had to be implemented. 

The tests were not easy to follow because those didn't have enough asserts.

Specific classes (e.g. Commerciant) were in the skel since the start of my walkthrough
because that information was randomly put in the assignment, CommerciantInput wasn't
supposed to be in the skel in the first place unless the input uses it. After I saw the 2nd stage of the assignment, 
I decided to keep the Commerciant class for future use.








