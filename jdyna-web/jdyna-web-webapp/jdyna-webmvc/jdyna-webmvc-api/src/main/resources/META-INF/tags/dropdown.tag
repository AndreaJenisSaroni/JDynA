<%--
-JDynA, Dynamic Metadata Management for Java Domain Object
-
- Copyright (c) 2008, CILEA and third-party contributors as
- indicated by the @author tags or express copyright attribution
- statements applied by the authors.  All third-party contributions are
- distributed under license by CILEA.
-
- This copyrighted material is made available to anyone wishing to use, modify,
- copy, or redistribute it subject to the terms and conditions of the GNU
- Lesser General Public License v3 or any later version, as published 
- by the Free Software Foundation, Inc. <http://fsf.org/>.
-
- This program is distributed in the hope that it will be useful,
- but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
- or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
- for more details.
-
-  You should have received a copy of the GNU Lesser General Public License
-  along with this distribution; if not, write to:
-  Free Software Foundation, Inc.
-  51 Franklin Street, Fifth Floor
-  Boston, MA  02110-1301  USA
--%>
<%@ attribute name="propertyPath" required="true"%>
<%@ attribute name="id" required="false"%>
<%@ attribute name="label" required="false"%>
<%@ attribute name="labelKey" required="false"%>
<%@ attribute name="help" required="false"%>
<%@ attribute name="helpKey" required="false"%>
<%@ attribute name="collection" required="true" type="java.util.Collection" %>
<%@ attribute name="required" required="false" type="java.lang.Boolean" %>
<%@ attribute name="ajaxValidation" required="false" description="javascript function name to make for validation ajax"%>
<%@ attribute name="validationParams" required="false" type="java.util.Collection" description="parameters of javascript function for ajax validation"%>
<%@ attribute name="option4row" required="false" type="java.lang.Integer" %>
<%@ attribute name="onchange" required="false"%>
<%-- eventi js non gestiti 
<%@ attribute name="onclick" required="false"%>
<%@ attribute name="onblur" required="false"%> 

<%@ attribute name="ondblclick" required="false"%>
<%@ attribute name="onkeydown" required="false"%>
<%@ attribute name="onkeyup" required="false"%>
<%@ attribute name="onkeypress" required="false"%>
<%@ attribute name="onfocus" required="false"%>
<%@ attribute name="onhelp" required="false"%>
<%@ attribute name="onselect" required="false"%>
<%@ attribute name="onmouseup" required="false"%>
<%@ attribute name="onmousedown" required="false"%>
<%@ attribute name="onmouseout" required="false"%>
<%@ attribute name="onmousemove" required="false"%>
<%@ attribute name="onmouseover" required="false"%>
--%>

<%@ taglib uri="jdynatags" prefix="dyna"%>
<%@ include file="/META-INF/taglibs4dynatag.jsp"%>

<c:if test="${label != null || labelKey != null}">
	<dyna:label label="${label}" labelKey="${labelKey}" 
		help="${help}" helpKey="${helpKey}" 
		propertyPath="${propertyPath}" required="${required}" />
</c:if>

<%-- FIXME: CODICE COMUNE A TUTTI I TAG... --%>

<c:set var="objectPath" value="${dyna:getObjectPath(propertyPath)}" />

<spring:bind path="${objectPath}">
	<c:set var="object" value="${status.value}" />
	<c:if test="${object == null}">
		<%-- Bind ignores the command object prefix, so simple properties of the command object return null above. --%>
		<c:set var="object" value="${commandObject}" />
		<%-- We depend on the controller adding this to request. --%>
	</c:if>
</spring:bind>

<c:set var="propertyName" value="${dyna:getPropertyName(propertyPath)}" />
<%-- FINE CODICE COMUNE A TUTTI I TAG... --%>

<!-- we need an indexed property -->
<c:catch var="exNoIndexedValue">
<spring:bind path="${propertyPath}[0]">
	<c:set var="value" value="${status.value}" />
	<c:set var="inputName"><c:out value="${status.expression}" escapeXml="false"></c:out></c:set>		
</spring:bind>
</c:catch>
<c:if test="${exNoIndexedValue != null}">
<spring:bind path="${propertyPath}">
	<c:set var="value" value="${status.value}" />
	<c:set var="inputName"><c:out value="${status.expression}" escapeXml="false"></c:out></c:set>		
</spring:bind>
</c:if>
<c:set var="onchangeValidation" value=""/>
<c:set var="parametersValidation" value="${dyna:extractParameters(validationParams)}"/>
<c:set var="functionValidation" value="${ajaxValidation}('${inputName}',${parametersValidation})" />
<c:if test="${!empty parametersValidation || !empty onchange}">
	<c:choose>
	<c:when test="${empty onchange}">
		<c:set var="onchangeValidation" value=" onchange=\"${functionValidation}\""/>
	</c:when>
	<c:otherwise>
		<c:choose>
		<c:when test="${empty parametersValidation}">
			<c:set var="onchangeValidation" value=" onchange=\"${onchange}\""/>
		</c:when>
		<c:otherwise>
			<c:set var="onchangeValidation" value=" onchange=\"${functionValidation};${onchange}\""/>
		</c:otherwise>
		</c:choose>
	</c:otherwise>
	
	</c:choose>
		
</c:if>

<select class="jdynadropdown" id="${!empty id?id:inputName}" name="${!empty inputName?inputName:propertyPath}" ${onchangeValidation}>
	<c:if test="${!empty collection}">
	    <c:forEach var="option" items="${collection}" varStatus="loop">	    	
            <option value="${option.identifyingValue}"<c:if test="${!empty value && option.identifyingValue == value}"> selected="selected"</c:if>>${option.displayValue}</option>     	
        </c:forEach>        
    </c:if>
</select>

<dyna:validation propertyPath="${propertyPath}" />
