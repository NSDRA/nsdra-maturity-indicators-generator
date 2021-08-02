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

package io.github.ammar257ammar.maturityIndicatorsGenerator;

import java.io.IOException;
import java.nio.file.FileSystems;

/**
 * The main class that performs the tasks passed through command line
 * 
 * @author Ammar Ammar
 *
 */

public class App 
{
    public static void main( String[] args ) throws IOException
    {   
        CliOptions cli = new CliOptions(args);
    	
    	if(!"".equals(cli.sourcePath.trim()) && !"".equals(cli.destPath.trim())) {
    	
    		int updatedCount = MiUtils.generateMaturityIndicators(cli.sourcePath, cli.destPath, 0);
            System.out.println(updatedCount + " maturity indicators updated");
            
            MiUtils.generateJsonFromMiLists(cli.sourcePath, cli.destPath + FileSystems.getDefault().getSeparator() + "lists.json");
    	}  
    }
}