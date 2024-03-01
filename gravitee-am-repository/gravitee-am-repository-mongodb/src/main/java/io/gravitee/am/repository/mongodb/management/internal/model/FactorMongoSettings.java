/**
 * Copyright (C) 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gravitee.am.repository.mongodb.management.internal.model;

import io.gravitee.am.model.ApplicationFactorSettings;
import io.gravitee.am.model.FactorSettings;
import io.gravitee.am.model.StepUpAuthenticationSettings;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FactorMongoSettings {
    private String defaultFactorId;
    private List<ApplicationFactorSettings> factors;

    public FactorSettings convert() {
        var factorSettings = new FactorSettings();
        factorSettings.setDefaultFactorId(defaultFactorId);
        factorSettings.setApplicationFactors(factors);
        return factorSettings;
    }

    public static FactorMongoSettings convert(FactorSettings factorSettings) {
        var factorMongoSettings = new FactorMongoSettings();
        factorMongoSettings.setDefaultFactorId(factorSettings.getDefaultFactorId());
        factorMongoSettings.setFactors(factorSettings.getApplicationFactors());
        return factorMongoSettings;
    }
}
