/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.w2m.spacecraft.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.w2m.spacecraft.infrastructure.rest.dto.SpacecraftRQDTO;
import com.w2m.spacecraft.infrastructure.rest.dto.SpacecraftRSDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import static org.instancio.Instancio.create;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SpacecraftControllerIntegrationTest {

	private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	private static final String URL_SPACECRAFT = "/spacecrafts";
	private static final String PATH_PARAM_ID = "/{id}";
	private static final String QUERY_PARAM_NAME = "name";
	private static final String QUERY_PARAM_PAGE = "page";
	private static final String QUERY_PARAM_SIZE = "size";

	private static final String EXPRESSION_ID = "$.id";
	private static final String EXPRESSION_NAME = "$.name";
	private static final String EXPRESSION_SERIES = "$.series";
	private static final String EXPRESSION_FILMS = "$.films";
	private static final String EXPRESSION_TOTAL_ITEMS = "$.totalItems";
	private static final String EXPRESSION_ITEMS_ID = "$.items[0].id";
	private static final String EXPRESSION_ITEMS_NAME = "$.items[0].name";
	private static final String EXPRESSION_ITEMS_SERIES = "$.items[0].series";
	private static final String EXPRESSION_ITEMS_FILMS = "$.items[0].films";
	private static final String EXPRESSION_TOTAL_PAGES = "$.totalPages";
	private static final String EXPRESSION_CURRENT_PAGE = "$.currentPage";


	private static final String TIMESTAMP_EXPRESSION = "$.timestamp";
	private static final String EXPRESSION_ERROR_CODE = "$.error.code";
	private static final String EXPRESSION_ERROR_MESSAGE = "$.error.message";

	private static final ResultMatcher OK = status().isOk();
	private static final ResultMatcher NOT_FOUND = status().isNotFound();
	private static final ResultMatcher MATCHER_APPLICATION_JSON = content().contentType("application/json");

	private static final long ID_NOT_FOUND = 99L;

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("Should create spacecraft")
	void shouldCreateSpacecraft() throws Exception {

		var spacecraftRQDTO = create(SpacecraftRQDTO.class);
		var requestJson = getObjectString(spacecraftRQDTO);

		this.mockMvc.perform(post(URL_SPACECRAFT)
				.contentType(APPLICATION_JSON_UTF8)
				.content(requestJson))
				.andDo(print())
				.andExpect(OK)
				.andExpect(MATCHER_APPLICATION_JSON)
				.andExpect(jsonPath(EXPRESSION_ID).exists())
				.andExpect(jsonPath(EXPRESSION_NAME).value(spacecraftRQDTO.getName()))
				.andExpect(jsonPath(EXPRESSION_SERIES).value(spacecraftRQDTO.getSeries()))
				.andExpect(jsonPath(EXPRESSION_FILMS).value(spacecraftRQDTO.getFilms()));

	}

	@Test
	@DisplayName("Should not find spacecraft")
	void shouldNotFindSpacecraft() throws Exception {

		this.mockMvc.perform(get(URL_SPACECRAFT + PATH_PARAM_ID, ID_NOT_FOUND))
				.andDo(print())
				.andExpect(NOT_FOUND)
				.andExpect(MATCHER_APPLICATION_JSON)
				.andExpect(jsonPath(TIMESTAMP_EXPRESSION).exists())
				.andExpect(jsonPath(EXPRESSION_ERROR_CODE).value("1"))
				.andExpect(jsonPath(EXPRESSION_ERROR_MESSAGE).value(String.format("Spacecraft selected with id: %s not found", ID_NOT_FOUND)));

	}

	@Test
	@DisplayName("Should find spacecraft")
	void shouldFindSpacecraft() throws Exception {

		var spacecraftRQDTO = create(SpacecraftRQDTO.class);

		var resultActionsCreate = postCreateSpacecraft(spacecraftRQDTO);

		var contentAsString = getReturnContentAsString(resultActionsCreate);
        var id = new ObjectMapper().readValue(contentAsString, SpacecraftRSDTO.class).getId();

		findById(id, spacecraftRQDTO);

	}

	@Test
	@DisplayName("Should not update spacecraft")
	void shouldNotUpdateSpacecraft() throws Exception {

		var spacecraftRQDTO = create(SpacecraftRQDTO.class);
		var requestJson = getObjectString(spacecraftRQDTO);

		this.mockMvc.perform(patch(URL_SPACECRAFT + PATH_PARAM_ID, ID_NOT_FOUND)
						.contentType(APPLICATION_JSON_UTF8)
						.content(requestJson))
				.andDo(print())
				.andExpect(NOT_FOUND)
				.andExpect(MATCHER_APPLICATION_JSON)
				.andExpect(jsonPath(TIMESTAMP_EXPRESSION).exists())
				.andExpect(jsonPath(EXPRESSION_ERROR_CODE).value("1"))
				.andExpect(jsonPath(EXPRESSION_ERROR_MESSAGE).value(String.format("Spacecraft selected with id: %s not found", ID_NOT_FOUND)));

	}

	@Test
	@DisplayName("Should update spacecraft")
	void shouldUpdateSpacecraft() throws Exception {

		var spacecraftRQDTO = create(SpacecraftRQDTO.class);
		var resultActionsCreate = postCreateSpacecraft(spacecraftRQDTO);

		var contentAsString = getReturnContentAsString(resultActionsCreate);
		var id = new ObjectMapper().readValue(contentAsString, SpacecraftRSDTO.class).getId();

		var spacecraftUpdateRQDTO = create(SpacecraftRQDTO.class);
		var requestJson = getObjectString(spacecraftUpdateRQDTO);

		this.mockMvc.perform(patch(URL_SPACECRAFT + PATH_PARAM_ID, id)
						.contentType(APPLICATION_JSON_UTF8)
						.content(requestJson))
				.andDo(print())
				.andExpect(OK)
				.andExpect(MATCHER_APPLICATION_JSON)
				.andExpect(jsonPath(EXPRESSION_ID).value(id))
				.andExpect(jsonPath(EXPRESSION_NAME).value(spacecraftUpdateRQDTO.getName()))
				.andExpect(jsonPath(EXPRESSION_SERIES).value(spacecraftUpdateRQDTO.getSeries()))
				.andExpect(jsonPath(EXPRESSION_FILMS).value(spacecraftUpdateRQDTO.getFilms()));
	}

	@Test
	@DisplayName("Should not delete spacecraft")
	void shouldNotDeleteSpacecraft() throws Exception {

		this.mockMvc.perform(delete(URL_SPACECRAFT + PATH_PARAM_ID, ID_NOT_FOUND))
				.andDo(print())
				.andExpect(NOT_FOUND)
				.andExpect(jsonPath(TIMESTAMP_EXPRESSION).exists())
				.andExpect(jsonPath(EXPRESSION_ERROR_CODE).value("1"))
				.andExpect(jsonPath(EXPRESSION_ERROR_MESSAGE).value(String.format("Spacecraft selected with id: %s not found", ID_NOT_FOUND)));

	}

	@Test
	@DisplayName("Should delete spacecraft")
	void shouldDeleteSpacecraft() throws Exception {

		var spacecraftRQDTO = create(SpacecraftRQDTO.class);
		var requestJson = getObjectString(spacecraftRQDTO);

		var resultActionsCreate = this.mockMvc.perform(post(URL_SPACECRAFT)
						.contentType(APPLICATION_JSON_UTF8)
						.content(requestJson))
				.andDo(print())
				.andExpect(OK)
				.andExpect(MATCHER_APPLICATION_JSON)
				.andExpect(jsonPath(EXPRESSION_ID).exists())
				.andExpect(jsonPath(EXPRESSION_NAME).value(spacecraftRQDTO.getName()))
				.andExpect(jsonPath(EXPRESSION_SERIES).value(spacecraftRQDTO.getSeries()))
				.andExpect(jsonPath(EXPRESSION_FILMS).value(spacecraftRQDTO.getFilms()));

		var contentAsString = getReturnContentAsString(resultActionsCreate);
		var id = new ObjectMapper().readValue(contentAsString, SpacecraftRSDTO.class).getId();

		this.mockMvc.perform(delete(URL_SPACECRAFT + PATH_PARAM_ID, id))
				.andDo(print())
				.andExpect(OK);

		this.mockMvc.perform(get(URL_SPACECRAFT + PATH_PARAM_ID, id))
				.andDo(print())
				.andExpect(NOT_FOUND)
				.andExpect(MATCHER_APPLICATION_JSON)
				.andExpect(jsonPath(TIMESTAMP_EXPRESSION).exists())
				.andExpect(jsonPath(EXPRESSION_ERROR_CODE).value("1"))
				.andExpect(jsonPath(EXPRESSION_ERROR_MESSAGE).value(String.format("Spacecraft selected with id: %s not found", id)));
	}

	@Test
	@DisplayName("should find spacecrafts by name with right pagination")
	void shouldFindSpacecraftsByNameWithRightPagination() throws Exception {

		var nameForSearch = "SelectedForQuery";
		var nameForElement = "patternSelectedForQuery";
		var anotherName = "trollName";

		var spacecraftRQDTO1 = create(SpacecraftRQDTO.class);
		spacecraftRQDTO1.setName(nameForElement);
		var resultActionsCreate1 = postCreateSpacecraft(spacecraftRQDTO1);
		var spacecraftRSDTO1 = new ObjectMapper().readValue(getReturnContentAsString(resultActionsCreate1),
				SpacecraftRSDTO.class);

		var spacecraftRQDTO2 = create(SpacecraftRQDTO.class);
		spacecraftRQDTO2.setName(nameForElement);
		var resultActionsCreate2 = postCreateSpacecraft(spacecraftRQDTO2);
		var spacecraftRSDTO2 = new ObjectMapper().readValue(getReturnContentAsString(resultActionsCreate2),
				SpacecraftRSDTO.class);

		var spacecraftRQDTO3 = create(SpacecraftRQDTO.class);
		spacecraftRQDTO3.setName(anotherName);
		postCreateSpacecraft(spacecraftRQDTO3);


		// first element in first page
		this.mockMvc.perform(get(URL_SPACECRAFT)
						.param(QUERY_PARAM_NAME, nameForSearch)
						.param(QUERY_PARAM_PAGE, "0")
						.param(QUERY_PARAM_SIZE, "1")
				)
				.andDo(print())
				.andExpect(OK)
				.andExpect(MATCHER_APPLICATION_JSON)
				.andExpect(jsonPath(EXPRESSION_TOTAL_ITEMS).value(2))
				.andExpect(jsonPath(EXPRESSION_ITEMS_ID).value(spacecraftRSDTO1.getId()))
				.andExpect(jsonPath(EXPRESSION_ITEMS_NAME).value(spacecraftRSDTO1.getName()))
				.andExpect(jsonPath(EXPRESSION_ITEMS_SERIES).value(spacecraftRSDTO1.getSeries()))
				.andExpect(jsonPath(EXPRESSION_ITEMS_FILMS).value(spacecraftRSDTO1.getFilms()))
				.andExpect(jsonPath(EXPRESSION_TOTAL_PAGES).value(2))
				.andExpect(jsonPath(EXPRESSION_CURRENT_PAGE).value(0));

		// second element in second page
		this.mockMvc.perform(get(URL_SPACECRAFT)
						.param(QUERY_PARAM_NAME, nameForSearch)
						.param(QUERY_PARAM_PAGE, "1")
						.param(QUERY_PARAM_SIZE, "1")
				)
				.andDo(print())
				.andExpect(OK)
				.andExpect(MATCHER_APPLICATION_JSON)
				.andExpect(jsonPath(EXPRESSION_TOTAL_ITEMS).value(2))
				.andExpect(jsonPath(EXPRESSION_ITEMS_ID).value(spacecraftRSDTO2.getId()))
				.andExpect(jsonPath(EXPRESSION_ITEMS_NAME).value(spacecraftRSDTO2.getName()))
				.andExpect(jsonPath(EXPRESSION_ITEMS_SERIES).value(spacecraftRSDTO2.getSeries()))
				.andExpect(jsonPath(EXPRESSION_ITEMS_FILMS).value(spacecraftRSDTO2.getFilms()))
				.andExpect(jsonPath(EXPRESSION_TOTAL_PAGES).value(2))
				.andExpect(jsonPath(EXPRESSION_CURRENT_PAGE).value(1));


	}

	private ResultActions postCreateSpacecraft(SpacecraftRQDTO spacecraftRQDTO) throws Exception {
		return this.mockMvc.perform(post(URL_SPACECRAFT)
						.contentType(APPLICATION_JSON_UTF8)
						.content(getObjectString(spacecraftRQDTO)))
				.andDo(print())
				.andExpect(OK)
				.andExpect(MATCHER_APPLICATION_JSON)
				.andExpect(jsonPath(EXPRESSION_ID).exists())
				.andExpect(jsonPath(EXPRESSION_NAME).value(spacecraftRQDTO.getName()))
				.andExpect(jsonPath(EXPRESSION_SERIES).value(spacecraftRQDTO.getSeries()))
				.andExpect(jsonPath(EXPRESSION_FILMS).value(spacecraftRQDTO.getFilms()));
	}

	private void findById(Long id, SpacecraftRQDTO spacecraftRQDTO) throws Exception {
		this.mockMvc.perform(get(URL_SPACECRAFT + PATH_PARAM_ID, id))
				.andDo(print())
				.andExpect(OK)
				.andExpect(MATCHER_APPLICATION_JSON)
				.andExpect(jsonPath(EXPRESSION_ID).value(id))
				.andExpect(jsonPath(EXPRESSION_NAME).value(spacecraftRQDTO.getName()))
				.andExpect(jsonPath(EXPRESSION_SERIES).value(spacecraftRQDTO.getSeries()))
				.andExpect(jsonPath(EXPRESSION_FILMS).value(spacecraftRQDTO.getFilms()));
	}

	private String getReturnContentAsString(ResultActions resultActions) throws UnsupportedEncodingException {
		return resultActions.andReturn().getResponse().getContentAsString();
	}

	private String getObjectString(Object object) throws JsonProcessingException {
		var mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		var ow = mapper.writer().withDefaultPrettyPrinter();
		return ow.writeValueAsString(object);
	}


}
