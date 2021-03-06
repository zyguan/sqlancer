package sqlancer.postgres.gen;

import java.util.Arrays;
import java.util.stream.Collectors;

import sqlancer.Query;
import sqlancer.QueryAdapter;
import sqlancer.Randomly;
import sqlancer.postgres.PostgresGlobalState;

public final class PostgresTruncateGenerator {

    private PostgresTruncateGenerator() {
    }

    public static Query create(PostgresGlobalState globalState) {
        StringBuilder sb = new StringBuilder();
        sb.append("TRUNCATE");
        if (Randomly.getBoolean()) {
            sb.append(" TABLE");
        }
        // TODO partitions
        // if (Randomly.getBoolean()) {
        // sb.append(" ONLY");
        // }
        sb.append(" ");
        sb.append(globalState.getSchema().getDatabaseTablesRandomSubsetNotEmpty().stream().map(t -> t.getName())
                .collect(Collectors.joining(", ")));
        if (Randomly.getBoolean()) {
            sb.append(" ");
            sb.append(Randomly.fromOptions("RESTART IDENTITY", "CONTINUE IDENTITY"));
        }
        if (Randomly.getBoolean()) {
            sb.append(" ");
            sb.append(Randomly.fromOptions("CASCADE", "RESTRICT"));
        }
        return new QueryAdapter(sb.toString(),
                Arrays.asList("cannot truncate a table referenced in a foreign key constraint", "is not a table"));
    }

}
