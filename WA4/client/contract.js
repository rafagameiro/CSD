function validate(username, balance, nTransfers, operation) {
    switch (operation) {
        // balance operation always executed
        case 0:
            return true;
        // deposit operation always executed
        case 1:
            return true;
            // a user is only allowed to transfer money if he has deposited more than 300
        case 2:
            return balance > 300;
        // a user is only allowed to consult the ledger if he has deposited more than 300
        // and has participated in 5 transfers
        case 3:
        case 4:
            return balance > 300 && nTransfers > 4;
        default:
            return false;
    }
}