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
package io.gravitee.am.reporter.kafka.audit;

import io.gravitee.am.common.utils.GraviteeContext;
import io.gravitee.am.model.ReferenceType;
import io.gravitee.am.reporter.api.audit.model.Audit;
import io.gravitee.am.reporter.api.audit.model.AuditAccessPoint;
import io.gravitee.am.reporter.api.audit.model.AuditEntity;
import io.gravitee.am.reporter.kafka.dto.AuditAccessPointDto;
import io.gravitee.am.reporter.kafka.dto.AuditEntityDto;
import io.gravitee.am.reporter.kafka.dto.AuditMessageValueDto;
import io.gravitee.node.api.Node;
import lombok.experimental.UtilityClass;
import org.apache.commons.validator.routines.InetAddressValidator;

@UtilityClass
public class DtoMapper {

    public static AuditMessageValueDto map(Audit audit, GraviteeContext context, Node node) {
        AuditMessageValueDto entry = new AuditMessageValueDto();
        entry.setId(audit.getId());
        entry.setReferenceId(audit.getReferenceId());
        entry.setReferenceType(mapReferenceType(audit.getReferenceType()));
        entry.setTimestamp(audit.timestamp());
        entry.setTransactionId(audit.getTransactionId());
        entry.setType(audit.getType());

        if (audit.getOutcome() != null) {
            // do not copy message part of the status
            entry.setStatus(audit.getOutcome().getStatus());
        }

        // copy access point and replace invalid IP
        AuditAccessPoint accessPoint = audit.getAccessPoint();
        if (accessPoint != null) {
            entry.setAccessPoint(mapAuditAccessPoint(accessPoint));
        }

        AuditEntity actor = audit.getActor();
        if (actor != null) {
            entry.setActor(mapAuditEntityDto(audit.getActor()));
        }

        AuditEntity target = audit.getTarget();
        if (target != null) {
            entry.setTarget(mapAuditEntityDto(target));
        }

        // link event to the organization and to the environment
        if (context != null) {
            if (context.getOrganizationId() != null) {
                entry.setOrganizationId(context.getOrganizationId());
            }
            if (context.getEnvironmentId() != null) {
                entry.setEnvironmentId(context.getEnvironmentId());
            }
            if (context.getDomainId() != null) {
                entry.setDomainId(context.getDomainId());
            }
        }


        // add node information
        if (node != null) {
            entry.setNodeId(node.id());
            entry.setNodeHostname(node.hostname());
        }

        return entry;
    }

    public static String mapReferenceType(ReferenceType referenceType) {
        return referenceType != null ? referenceType.toString().toUpperCase() : null;
    }

    public static AuditEntityDto mapAuditEntityDto(AuditEntity auditEntity) {
        AuditEntityDto dto = new AuditEntityDto();
        dto.setId(auditEntity.getId());
        dto.setType(auditEntity.getType());
        dto.setReferenceId(auditEntity.getReferenceId());
        dto.setReferenceType(mapReferenceType(auditEntity.getReferenceType()));
        dto.setAlternativeId(auditEntity.getAlternativeId());
        dto.setDisplayName(auditEntity.getDisplayName());
        return dto;
    }

    public static AuditAccessPointDto mapAuditAccessPoint(AuditAccessPoint accessPoint) {
        AuditAccessPointDto dto = new AuditAccessPointDto();
        dto.setId(accessPoint.getId());
        dto.setAlternativeId(accessPoint.getAlternativeId());
        dto.setDisplayName(accessPoint.getDisplayName());
        dto.setIpAddress(accessPoint.getIpAddress());
        dto.setUserAgent(accessPoint.getUserAgent());

        if (accessPoint.getIpAddress() != null && !InetAddressValidator.getInstance()
                .isValid(accessPoint.getIpAddress())) {
            dto.setIpAddress("0.0.0.0");
        }
        return dto;
    }
}
