/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.elasticsearch.client.ml;

import org.elasticsearch.client.ml.dataframe.evaluation.EvaluationMetric;
import org.elasticsearch.client.ml.dataframe.evaluation.MlEvaluationNamedXContentProvider;
import org.elasticsearch.client.ml.dataframe.evaluation.regression.MeanSquaredErrorMetricResultTests;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.test.AbstractXContentTestCase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class EvaluateDataFrameResponseTests extends AbstractXContentTestCase<EvaluateDataFrameResponse> {

    public static EvaluateDataFrameResponse randomResponse() {
        List<EvaluationMetric.Result> metrics = new ArrayList<>();
        if (randomBoolean()) {
            metrics.add(AucRocMetricResultTests.randomResult());
        }
        if (randomBoolean()) {
            metrics.add(PrecisionMetricResultTests.randomResult());
        }
        if (randomBoolean()) {
            metrics.add(RecallMetricResultTests.randomResult());
        }
        if (randomBoolean()) {
            metrics.add(ConfusionMatrixMetricResultTests.randomResult());
        }
        if (randomBoolean()) {
            metrics.add(MeanSquaredErrorMetricResultTests.randomResult());
        }
        return new EvaluateDataFrameResponse(randomAlphaOfLength(5), metrics);
    }

    @Override
    protected EvaluateDataFrameResponse createTestInstance() {
        return randomResponse();
    }

    @Override
    protected EvaluateDataFrameResponse doParseInstance(XContentParser parser) throws IOException {
        return EvaluateDataFrameResponse.fromXContent(parser);
    }

    @Override
    protected boolean supportsUnknownFields() {
        return true;
    }

    @Override
    protected Predicate<String> getRandomFieldsExcludeFilter() {
        // allow unknown fields in the metrics map (i.e. alongside named metrics like "precision" or "recall")
        return field -> field.isEmpty() || field.contains(".");
    }

    @Override
    protected NamedXContentRegistry xContentRegistry() {
        return new NamedXContentRegistry(new MlEvaluationNamedXContentProvider().getNamedXContentParsers());
    }
}
