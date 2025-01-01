package com.example.analytics;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import org.apache.flink.orc.vector.Vectorizer;
import org.apache.hadoop.hive.ql.exec.vector.BytesColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.ListColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.LongColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.TimestampColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.VectorizedRowBatch;

/**
 * Vectorizer for {@link ApiUsage} to handle the lifecycle of
 * {@link VectorizedRowBatch}.
 */
public class ApiUsageVectorizer extends Vectorizer<ApiUsage> {

    private static final long serialVersionUID = -3256900631626158235L;
    private static final String API_USAGE_SCHEME = "struct<"
            + "requestType:string,"
            + "userId:string,"
            + "requestTime:timestamp,"
            + "requestFields:array<string>,"
            + "deprecated:boolean,"
            + "resultCode:string,"
            + "oneTimeEvent:boolean,"
            + "failedRequest:boolean>";

    /** Constructor for {@link ApiUsageVectorizer}. */
    public ApiUsageVectorizer() {
        super(API_USAGE_SCHEME);
    }

    @Override
    public void vectorize(final ApiUsage apiUsage, final VectorizedRowBatch batch) {
        final int row = batch.size++;
        final BytesColumnVector requestTypeColVector = (BytesColumnVector) batch.cols[0];
        requestTypeColVector.setVal(row, apiUsage.getRequestType().toString().getBytes(UTF_8));

        final BytesColumnVector userIdColVector = (BytesColumnVector) batch.cols[1];
        userIdColVector.setVal(row, apiUsage.getUserId().getBytes(UTF_8));

        final TimestampColumnVector requestTimeColVector = (TimestampColumnVector) batch.cols[2];
        requestTimeColVector.set(row, Timestamp.from(apiUsage.getRequestTime()));

        final ListColumnVector requestFieldsColVector = (ListColumnVector) batch.cols[3];
        final List<String> fields = Optional.ofNullable(apiUsage.getRequestFields()).orElse(List.of());
        final int requestFieldsSize = fields.size();

        // Set the offsets and lengths
        final int offset = requestFieldsColVector.childCount;
        requestFieldsColVector.offsets[row] = offset;
        requestFieldsColVector.lengths[row] = requestFieldsSize;

        // Increase the childCount by the number of elements for this row
        requestFieldsColVector.childCount += requestFieldsSize;

        // Ensure child vector capacity
        requestFieldsColVector.child.ensureSize(requestFieldsColVector.childCount, false);

        // Write each request field at the correct position
        final BytesColumnVector requestFieldVector = (BytesColumnVector) requestFieldsColVector.child;
        for (int i = 0; i < requestFieldsSize; i++) {
            requestFieldVector.setVal(offset + i, fields.get(i).getBytes(UTF_8));
        }

        final LongColumnVector deprecatedColVector = (LongColumnVector) batch.cols[4];
        deprecatedColVector.vector[row] = apiUsage.isDeprecated() ? 1 : 0;

        final BytesColumnVector resultCodeColVector = (BytesColumnVector) batch.cols[5];
        resultCodeColVector.setVal(row, apiUsage.getResultCode().getBytes(UTF_8));

        final LongColumnVector isOneTimeEventColVector = (LongColumnVector) batch.cols[6];
        isOneTimeEventColVector.vector[row] = defaultIfNull(apiUsage.getOneTimeEvent(), false) ? 1 : 0;

        final LongColumnVector isFailedRequestColVector = (LongColumnVector) batch.cols[7];
        isFailedRequestColVector.vector[row] = defaultIfNull(apiUsage.isFailedRequest(), false) ? 1 : 0;
    }
}
