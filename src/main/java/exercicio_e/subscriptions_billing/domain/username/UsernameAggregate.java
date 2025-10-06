package exercicio_e.subscriptions_billing.domain.username;

import exercicio_e.subscriptions_billing.domain.exception.DomainException;
import exercicio_e.subscriptions_billing.domain.exception.InvalidUsernameException;
import exercicio_e.subscriptions_billing.domain.exception.UsernameUnavailableException;
import exercicio_e.subscriptions_billing.domain.username.event.UsernameEvent;
import exercicio_e.subscriptions_billing.domain.username.event.UsernameEvent.UsernameClaimed;
import exercicio_e.subscriptions_billing.domain.username.event.UsernameEvent.UsernameReleased;
import exercicio_e.subscriptions_billing.domain.username.event.UsernameEvent.UsernameReserved;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class UsernameAggregate {

    private final String usernameKey;
    private long version;
    private UsernameStatus status;
    private UUID claimedBy;

    private UsernameAggregate(String usernameKey) {
        validateKey(usernameKey);
        this.usernameKey = usernameKey;
    }

    public static UsernameAggregate from(String usernameKey, List<UsernameEvent> history, long lastVersion) {
        final UsernameAggregate aggregate = new UsernameAggregate(usernameKey);
        aggregate.replay(history);
        aggregate.version = lastVersion;
        return aggregate;
    }

    private void replay(List<UsernameEvent> eventStream) {
        for (UsernameEvent event : eventStream) {
            switch (event) {
                case UsernameReserved ignored -> apply(ignored);
                case UsernameClaimed e -> apply(e);
                case UsernameReleased ignored -> apply(ignored);
                default -> throw new DomainException("Unexpected value: " + event);
            }
        }
    }

    private void apply(UsernameReserved __) {
        this.status = UsernameStatus.RESERVED;
        this.claimedBy = null;
    }

    private void apply(UsernameClaimed event) {
        this.status = UsernameStatus.TAKEN;
        this.claimedBy = event.accountId();
    }

    private void apply(UsernameReleased __) {
        this.status = UsernameStatus.AVAILABLE;
        this.claimedBy = null;
    }

    public UsernameReserved decideReserve() {
        if (this.status == UsernameStatus.RESERVED) {
            throw new UsernameUnavailableException("Username is already reserved.");
        }
        if (this.status == UsernameStatus.TAKEN) {
            throw new UsernameUnavailableException("Username is already taken.");
        }
        return new UsernameReserved(usernameKey, Instant.now());
    }

    public UsernameClaimed decideClaim(String usernameKey, String accountId) {
        validateKey(usernameKey);
        if (accountId == null || accountId.isBlank()) {
            throw new RuntimeException("Invalid account ID.");
        }
        return new UsernameClaimed(usernameKey, UUID.fromString(accountId), Instant.now());
    }

    public UsernameReleased decideRelease(String usernameKey, String commandId, String reason) {
        validateKey(usernameKey);
        if (commandId == null || commandId.isBlank()) {
            throw new RuntimeException("Invalid command ID.");
        }
        if (reason == null || reason.isBlank()) {
            throw new RuntimeException("Invalid reason.");
        }
        return new UsernameReleased(usernameKey, Instant.now(), reason);
    }

    private void validateKey(String usernameKey) {
        if (usernameKey == null || usernameKey.isBlank()) {
            throw new InvalidUsernameException("O nome de usuário não pode ser nulo ou vazio.");
        }
    }

}
