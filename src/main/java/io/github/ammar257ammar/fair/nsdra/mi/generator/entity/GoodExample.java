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
 * A java entity class representing a JSON-LD good example of a maturity indicator
 * 
 * @author Ammar Ammar
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoodExample {
	
	@JsonProperty("@id")
    private String id;

	@JsonProperty("https://schema.org/type")
    private String type;

    @JsonProperty("https://schema.org/name")
    private String name;
    
    @JsonProperty("https://schema.org/url")
    private String url;

    @JsonProperty("https://schema.org/variableMeasured")
    private VariableMeasured variableMeasured = new VariableMeasured();

    public GoodExample() {}
    
	public GoodExample(String id, String type, String name, VariableMeasured variables) {
		this.id = id;
		this.type = type;
		this.name = name;
		this.variableMeasured = variables;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public VariableMeasured getVariableMeasured() {
		return variableMeasured;
	}

	public void setVariableMeasured(VariableMeasured variables) {
		this.variableMeasured = variables;
	}

	@Override
	public String toString() {
		return "GoodExample [id=" + id + ", type=" + type + ", name=" + name + "]";
	}
}
