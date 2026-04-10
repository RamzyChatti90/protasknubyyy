package com.protasknubyyy.service.mapper;

import com.protasknubyyy.domain.Project;
import com.protasknubyyy.service.dto.ProjectDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Project} and its DTO {@link ProjectDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProjectMapper extends EntityMapper<ProjectDTO, Project> {}
