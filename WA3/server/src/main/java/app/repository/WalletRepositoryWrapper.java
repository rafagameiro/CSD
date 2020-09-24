package app.repository;

import app.models.Wallet;
import org.springframework.data.repository.CrudRepository;

public interface WalletRepositoryWrapper extends CrudRepository<Wallet, String> {

}
