/*
 * Copyright 2012 - 2015 pac4j organization
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.pac4j.cas.client.rest;

import org.pac4j.core.client.ClientType;
import org.pac4j.http.credentials.authenticator.Authenticator;
import org.pac4j.http.credentials.extractor.FormExtractor;

/**
 * CAS rest client that uses a {@link FormExtractor}.
 *
 * @author Misagh Moayyed
 * @since 1.8.0
 */
public class CasRestFormClient extends AbstractCasRestClient {

    private String username = "username";

    private String password = "password";

    public CasRestFormClient(final Authenticator authenticator) {
        super(authenticator);
        this.extractor = new FormExtractor(username, password, CasRestFormClient.class.getSimpleName());
    }

    public CasRestFormClient(final Authenticator authenticator, final String username, final String password) {
        super(authenticator);
        this.username = username;
        this.password = password;
        this.extractor = new FormExtractor(username, password, CasRestFormClient.class.getSimpleName());
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    protected AbstractCasRestClient newClientType() {
        return new CasRestFormClient(getAuthenticator());
    }

    @Override
    public ClientType getClientType() {
        return ClientType.FORM_BASED;
    }
}
