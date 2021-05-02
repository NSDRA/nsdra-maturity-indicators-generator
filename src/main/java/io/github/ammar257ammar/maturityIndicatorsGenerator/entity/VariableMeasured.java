/**
* FAIR Maturity Indicators Generator for NanoSafety Data Reusability
* 
*Copyright (C) 2021  Ammar Ammar <ammar257ammar@gmail.com> ORCID:0000-0002-8399-8990
*
*This program is free software: you can redistribute it and/or modify
*it under the terms of the GNU Affero General Public License as published by
*the Free Software Foundation, either version 3 of the License, or
*(at your option) any later version.
*
*This program is distributed in the hope that it will be useful,
*but WITHOUT ANY WARRANTY; without even the implied warranty of
*MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*GNU Affero General Public License for more details.
*
*You should have received a copy of the GNU Affero General Public License
*along with this program.  If not, see <http://www.gnu.org/licenses/>.
*
*/

package io.github.ammar257ammar.maturityIndicatorsGenerator.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A java entity class representing the "VariableMeasured" schema.org object
 * 
 * @author Ammar Ammar
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class VariableMeasured {
	
	@JsonProperty("@type")
    private String type;
	
    @JsonProperty("https://schema.org/name")
    private String name;

    public VariableMeasured() {}
    
	public VariableMeasured(String type, String name) {
		this.type = type;
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "VariableMeasured [type=" + type + ", name=" + name + "]";
	}
}
