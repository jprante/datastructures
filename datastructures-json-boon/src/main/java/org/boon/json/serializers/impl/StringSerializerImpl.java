/*
 * Copyright 2013-2014 Richard M. Hightower
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * __________                              _____          __   .__
 * \______   \ ____   ____   ____   /\    /     \ _____  |  | _|__| ____    ____
 *  |    |  _//  _ \ /  _ \ /    \  \/   /  \ /  \\__  \ |  |/ /  |/    \  / ___\
 *  |    |   (  <_> |  <_> )   |  \ /\  /    Y    \/ __ \|    <|  |   |  \/ /_/  >
 *  |______  /\____/ \____/|___|  / \/  \____|__  (____  /__|_ \__|___|  /\___  /
 *         \/                   \/              \/     \/     \/       \//_____/
 *      ____.                     ___________   _____    ______________.___.
 *     |    |____ ___  _______    \_   _____/  /  _  \  /   _____/\__  |   |
 *     |    \__  \\  \/ /\__  \    |    __)_  /  /_\  \ \_____  \  /   |   |
 * /\__|    |/ __ \\   /  / __ \_  |        \/    |    \/        \ \____   |
 * \________(____  /\_/  (____  / /_______  /\____|__  /_______  / / ______|
 *               \/           \/          \/         \/        \/  \/
 */

package org.boon.json.serializers.impl;

import org.boon.Str;
import org.boon.json.serializers.JsonSerializerInternal;
import org.boon.json.serializers.StringSerializer;
import org.boon.primitive.CharBuf;

/**
 * Created by rick on 1/1/14.
 */
public class StringSerializerImpl implements StringSerializer {

    final boolean encodeStrings;
    final boolean asAscii;
    final boolean includeEmpty;

    public StringSerializerImpl(boolean encodeStrings, boolean asAscii, boolean includeEmpty) {

        this.encodeStrings = encodeStrings;
        this.asAscii = asAscii;
        this.includeEmpty = includeEmpty;
    }

    @Override
    public final void serializeString ( JsonSerializerInternal serializer, String string, CharBuf builder )  {
        if(includeEmpty || !Str.empty(string)){

            if (encodeStrings) {

                builder.asJsonString(string, asAscii);
            } else {
                builder.addQuoted(string);
            }
        }
    }
}
